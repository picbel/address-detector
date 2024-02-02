package util.trie

import org.json.JSONObject
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested

internal class TrieSpec {

    private val jsonString = """
    {
        "경": {
            "기": {
                "도": {
                    "value": "택배"
                },
                "value": "UNKNOWN"
            },
            "상": {
                "북": {
                    "value": "UNKNOWN",
                    "도" : {
                        "value": "택배"
                    }
                },
                "남": {
                    "value": "UNKNOWN",
                    "도" : {
                        "value": "택배"
                    }
                },
                "value": "UNKNOWN"
            },
            "value": "UNKNOWN"
        },
        "서": {
            "울": {
                "value": "UNKNOWN",
                "시": {"value": "지입배송"}
            },
            "value": "UNKNOWN"
        }
    }
    """.trimIndent()

    private val trie = Trie(jsonString)

    @DisplayName("Trie를 JSON 문자열로 변환합니다.")
    @Test
    fun toJsonString() {
        // given // when
        val jsonString = trie.toJsonString()
        // then
        assert(JSONObject(jsonString).toString() == JSONObject(this.jsonString).toString())
    }

    @DisplayName("가장 많이 매칭돈 문자열의 value를 반환합니다 : ")
    @Nested
    inner class FindSimilarValueSpec {
        @DisplayName("가장 많이 매칭된 문자열의 value를 반환합니다.")
        @Test
        fun allMatching() {
            trie.findSimilarValue("경기도")
            trie.findSimilarValue("서울시")?.let { println("서울시 : $it") }
        }
    }


}