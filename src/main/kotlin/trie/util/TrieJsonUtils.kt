package trie.util

import org.json.JSONObject
import trie.Trie
import trie.TrieImpl
import trie.MutableTrieNode
import trie.TrieNode


object TrieJsonUtils {
    private const val VALUE = "value"


    /**
     * @return Trie를 JSON 문자열로 변환합니다.
     */
    fun Trie.toJsonString(): String {
        return trieToJson(root).toString()
    }

    private fun trieToJson(node: TrieNode): JSONObject {
        val jsonObject = JSONObject()
        node.value?.let { jsonObject.put("value", it) }
        node.children.forEach { (char, childNode) ->
            jsonObject.put(char.toString(), trieToJson(childNode))
        }
        return jsonObject
    }

    /**
     * @return JSON 문자열로부터 Trie를 구축합니다.
     */
    fun Trie.Companion.fromJson(jsonStr: String): Trie {
        val jsonObject = JSONObject(jsonStr)
        val node = MutableTrieNode()
        buildTrieFromJson(jsonObject, node)
        return TrieImpl(node)
    }

    // JSON 객체로부터 Trie를 구축하는 수정된 메소드
    private fun buildTrieFromJson(jsonObject: JSONObject, node: MutableTrieNode) {
        jsonObject.keys().forEach { key ->
            when (key) {
                VALUE -> node.value = jsonObject.getString(key) ?: null
                else -> {
                    val childNode = MutableTrieNode()
                    node.children[key[0]] = childNode
                    buildTrieFromJson(jsonObject.getJSONObject(key), childNode)
                }
            }
        }
    }

}
