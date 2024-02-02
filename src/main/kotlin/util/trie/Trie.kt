package util.trie

import org.json.JSONObject

internal class Trie(
    jsonStr: String,
    /**
     * Trie에서 value로 취급하지 않을 문자열들
     */
    val ignoreValue: List<String> = listOf()
) {
    private val root = TrieNode()

    init {
        buildTrieFromJson(JSONObject(jsonStr))
    }

    // Trie를 JSON 문자열로 변환하는 메소드
    fun toJsonString(): String = trieToJson(root).toString(4)

    fun findSimilarValue(word: String): String? {
        var currentNode = root
        for (char in word) {
            val childNode = currentNode.children[char] ?: return null // 단어가 Trie에 없음
            currentNode = childNode
        }
        return currentNode.value
    }

//    fun addWord(word: String, value: String) : Boolean{
//        var currentNode = root
//        for (char in word) {
//            val childNode = currentNode.children[char] ?: TrieNode().also { currentNode.children[char] = it }
//            currentNode = childNode
//        }
//        if (currentNode.value != null) return false
//        currentNode.value = value
//        return true
//    }
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

    // JSON 객체로부터 Trie를 구축하는 수정된 메소드
    private fun buildTrieFromJson(jsonObject: JSONObject, node: TrieNode = root) {
        jsonObject.keys().forEach { key ->
            when (key) {
                "value" -> node.value = jsonObject.getString(key)
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
