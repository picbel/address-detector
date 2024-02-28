# Kotlin Trie with JSON Support

## 소개

이 프로젝트는 Kotlin으로 구현된 Trie 자료구조를 포함하며, Trie를 JSON으로 만들고 JSON에서 Trie를 복원하는 기능을 지원합니다.

## 기능

- **Trie 구현**: 문자열을 효율적으로 저장하고 검색할 수 있는 Trie 자료구조 구현.
- **JSON 지원**: Trie를 JSON 형태로 변환하고, JSON에서 Trie를 복원하는 기능.

### 사용 예제

Trie 생성 및 사용법:

```kotlin
val trie = Trie()
trie.put("hello", "value")
trie.findsimlirValue("hello")
```

Trie를 JSON으로 변환:

```kotlin
val json = trie.toJsonString()
println(json)
```

JSON에서 Trie 복원:

```kotlin
val newTrie = Trie.fromJson(json)
```

---


