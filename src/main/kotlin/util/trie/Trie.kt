package util.trie

import org.json.JSONObject

internal class Trie(
    jsonStr: String,
) {
    private val root = TrieNode()

    init {
        buildTrieFromJson(JSONObject(jsonStr))
    }

    /**
     * @param str 비교할 문자열
     * @return 해당 문자열중 가장 많이 매칭된 문자열의 value를 가져옵니다.
     */
    fun findSimilarValue(str: String): String? {
        var currentNode = root
        var value: String? = null
        for (char in str) {
            val childNode = currentNode.children[char] ?: break
            if (childNode.value != null) value = childNode.value
            currentNode = childNode
        }
        return value
    }

    /**
     * @param str 추가할 단어
     * @param value 단어에 대응하는 값
     */
    fun put(str: String, value: String): Boolean {
        var currentNode = root
        for (char in str) {
            val childNode = currentNode.children[char] ?: TrieNode().also { currentNode.children[char] = it }
            currentNode = childNode
        }
        if (currentNode.value != null) return false
        currentNode.value = value
        return true
    }

    //
//    fun deleteWord(word: String) : Boolean{
//        var currentNode = root
//        for (char in word) {
//            val childNode = currentNode.children[char] ?: return false
//            currentNode = childNode
//        }
//        currentNode.value = null
//        return true
//    }

    /**
     * @return Trie를 JSON 문자열로 변환합니다.
     */
    fun toJsonString(): String = trieToJson(root).toString(4)

    // JSON 객체로부터 Trie를 구축하는 수정된 메소드
    private fun buildTrieFromJson(jsonObject: JSONObject, node: TrieNode = root) {
        jsonObject.keys().forEach { key ->
            when (key) {
                "value" -> node.value = jsonObject.getString(key) ?: null
                else -> {
                    val childNode = TrieNode()
                    node.children[key[0]] = childNode
                    buildTrieFromJson(jsonObject.getJSONObject(key), childNode)
                }
            }
        }
    }

    // Trie를 JSON 객체로 변환하는 메소드
    private fun trieToJson(node: TrieNode): JSONObject {
        val jsonObject = JSONObject()
        node.value?.let { jsonObject.put("value", it) }
        node.children.forEach { (char, childNode) ->
            jsonObject.put(char.toString(), trieToJson(childNode))
        }
        return jsonObject
    }
}

private class TrieNode(
    var value: String? = null
) {
    val children: MutableMap<Char, TrieNode> = mutableMapOf()
}
