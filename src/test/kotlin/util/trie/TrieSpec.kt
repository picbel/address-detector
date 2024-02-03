package util.trie

import io.kotest.assertions.assertSoftly
import io.kotest.matchers.shouldBe
import org.json.JSONObject
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested

internal class TrieSpec {

    private val jsonString = """
    {
        "경": {
            "기": {
                "도": {
                    "value": "지입배송"
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
        @DisplayName("'경기도'로 조회시 value는 '택배'여야 합니다.")
        @Test
        fun allMatching() {
            trie.findSimilarValue("경기도") shouldBe "지입배송"
        }

        @DisplayName("'서울시강남'으로 조회시 value는 '직접배송'여야 합니다.")
        @Test
        fun similarMatching() {
            // 가장 비슷한 '서울시'의 값을 가져옵니다
            trie.findSimilarValue("서울시강남") shouldBe "직접배송"
        }

        @DisplayName("'뉴욕'으로 조회시 value가 없습니다.")
        @Test
        fun missing() {
            trie.findSimilarValue("뉴욕") shouldBe null
        }
    }

    @DisplayName("가장 많이 매칭된 문자열의 value를 반환합니다 : ")
    @Nested
    inner class PutSpec {
        @DisplayName("'제주도'를 추가합니다")
        @Test
        fun new() {
            // given
            val str = "제주도"
            val value = "배송불가"
            // when
            val result = trie.put(str, value)
            // then
            assertSoftly {
                result shouldBe true
                trie.findSimilarValue("제주도") shouldBe value
            }
        }

        @DisplayName("'서울시송파구'를 추가합니다.")
        @Test
        fun put() {
            // given
            val str = "서울시송파구" // 공백처리를 입력단에서 하는것으로 정함
            val value = "직접배송"
            // when
            val result = trie.put(str, value)
            // then
            assertSoftly {
                result shouldBe true
                trie.findSimilarValue("서울시송파구") shouldBe value
            }
            println(trie.toJsonString())
        }

        @DisplayName("'경상북도'의 value를 '택배'에서 '배송불가'로 변경합니다.'")
        @Test
        fun modify() {
            // given
            val str = "경상북도"
            val value = "배송불가"
            // when
            val result = trie.put(str, value)
            // then
            assertSoftly {
                result shouldBe true
                trie.findSimilarValue(str) shouldBe value
            }
            println(trie.toJsonString())
        }
    }


}