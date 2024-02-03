package util.trie

import io.kotest.matchers.shouldBe
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
                }
            },
            "상": {
                "북": {
                    "도" : {
                        "value": "택배"
                    }
                },
                "남": {
                    "도" : {
                        "value": "택배"
                    }
                }
            }
        },
        "서": {
            "울": {
                "시": {"value": "직접배송"}
            }
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

    @DisplayName("가장 많이 매칭된 문자열의 value를 반환합니다 : ")
    @Nested
    inner class FindSimilarValueSpec {
        @DisplayName("경기도로 조회시 value는 '택배'여야 합니다.")
        @Test
        fun allMatching() {
            trie.findSimilarValue("경기도") shouldBe "택배"
        }

        @DisplayName("'서울시강남'으로 조회시 value는 '직접배송'여야 합니다.")
        @Test
        fun similarMatching() {
            // 가장 비슷한 '서울시'의 값을 가져옵니다
            trie.findSimilarValue("서울시강남") shouldBe "직접배송"
        }
    }


}