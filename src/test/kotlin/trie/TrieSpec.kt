package trie

import io.kotest.assertions.assertSoftly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested

internal class TrieSpec {
    private val trie = MutableTrie.empty().apply {
        put("서울특별시", "직접배송")
        put("경기도", "지입배송")
        put("경상북도", "택배")
        put("경상남도", "택배")
        put("세종시", "지입배송")
    }

    @DisplayName("가장 많이 매칭된 문자열의 value를 반환합니다 : ")
    @Nested
    inner class FindSimilarValueSpec {
        @DisplayName("'경기도'로 조회시 value는 '택배'여야 합니다.")
        @Test
        fun allMatching() {
            trie.findSimilarValue("경기도") shouldBe "지입배송"
        }

        @DisplayName("'서울특별시강남'으로 조회시 value는 '직접배송'여야 합니다.")
        @Test
        fun similarMatching() {
            // 가장 비슷한 '서울특별시'의 값을 가져옵니다
            trie.findSimilarValue("서울특별시강남") shouldBe "직접배송"
        }

        @DisplayName("'뉴욕'으로 조회시 value가 없습니다.")
        @Test
        fun missing() {
            trie.findSimilarValue("뉴욕") shouldBe null
        }
    }

    @DisplayName("문자열을 추가 및 수정합니다 : ")
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

        @DisplayName("'서울특별시송파구'를 추가합니다.")
        @Test
        fun put() {
            // given
            val str = "서울특별시송파구" // 공백처리를 입력단에서 하는것으로 정함
            val value = "직접배송"
            // when
            val result = trie.put(str, value)
            // then
            assertSoftly {
                result shouldBe true
                trie.findSimilarValue(str) shouldBe value
            }
            listOf<Any>().toMutableList()
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
        }
    }

    @Nested
    inner class RemoveAndClearSpec {
        @DisplayName("문자열을 삭제합니다")
        @Test
        fun remove() {
            // given
            val str = "경상남도"
            // when
            val result = trie.remove(str)
            // then
            assertSoftly {
                result shouldBe true
                trie.findSimilarValue(str) shouldBe null
            }
        }

        @DisplayName("없는 문자열을 삭제 할 수 없습니다")
        @Test
        fun removeFail() {
            // given
            val str = "뉴욕"
            // when
            val result = trie.remove(str)
            // then
            result shouldBe false
        }

        @DisplayName("자식이 있는 문자열은 삭제 할 수 없습니다.")
        @Test
        fun removeFail2() {
            // given
            val str = "서울"
            // when
            val result = trie.remove(str)
            // then
            result shouldBe false
        }

        @DisplayName("문자열의 value를 삭제합니다.")
        @Test
        fun clearValue() {
            // given
            val str = "강원도"
            trie.put(str, "test")
            // when
            val result = trie.clearValue(str)
            // then
            assertSoftly {
                result shouldBe true
                trie.findSimilarValue(str) shouldBe null
            }
        }
    }

}