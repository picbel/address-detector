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
        currentNode.value = value
        return true
    }

    /**
     * 해당 문자열 구조를 trie에서 삭제합니다
     * 부모 노드가 자식 노드를 가지고 있지 않고 value가 null일 경우 삭제합니다.
     *
     * @param str 삭제할 문자열
     */
    fun remove(str: String): Boolean {
        // 삭제할 문자열의 노드를 저장합니다. first: 문자, second: 부모 노드
        val nodes = mutableListOf<Pair<Char, TrieNode>>()
        var currentNode = root
        for (char in str) {
            val childNode = currentNode.children[char] ?: return false
            nodes.add(char to currentNode)
            currentNode = childNode
        }
        if (currentNode.hasChildren()) return false
        for (i in nodes.size - 1 downTo 0) {
            val (char, parentNode) = nodes[i]
            if (currentNode.hasChildren() && currentNode.value != null) break
            parentNode.children.remove(char)
            currentNode = parentNode
        }
        return true
    }

    /**
     * 해당 문자열의 value를 삭제합니다
     * @param str
     */
    fun clearValue(str: String): Boolean {
        var currentNode = root
        for (char in str) {
            val childNode = currentNode.children[char] ?: return false
            currentNode = childNode
        }
        currentNode.value = null
        if (!currentNode.hasChildren()) remove(str) // value도 없고 자식node도 없다면 그냥 삭제
        return true
    }

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
    fun hasChildren() = children.isNotEmpty()
}
