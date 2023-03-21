# 카카오뱅크 사전과제 - 블로그 검색 API 개발
## 목차
- [개발 환경](#개발환경)
- [빌드 및 실행](#빌드-및-실행하기)
- [기능 요구사항](#기능-요구사항)
- [해결방법](#해결방법)


---

## 개발환경
- 기본 환경 
  - IDE : IntelliJ IDEA Ultimate
  - OS : Mac OS
  - GIT
- Server
  - Java 11
  - Spring Boot 2.7.9
  - JPA
  - H2
  - Gradle
  - JUnit5


## 빌드 및 실행하기
### 터미널 환경
- Git, Java는 설치되어 있다고 가정한다
```
$ git clone https://github.com/kim-sun-a/blog_search.git
$ cd blog_search
$ ./gredlew clean build
$ java -jar build/libs/search-0.0.1-SNAPSHOT.jar
```

- 접속 Base URI: `http://localhost:8080`


## 기능 요구사항
### 필수사항
- 블로그 검색 API
  - 키워드를 통해서 블로그를 검색할 수 있어야 한다
  - 검색 결과에서 Sorting 기능을 지원해야 한다
  - 검색 결과는 Pagination 형태로 제공해야 한다
- 인기 검색어 목록 API
  - 많이 검색한 순서대로 최대 10개의 검색 키워드를 제공한다
  - 검색어 별로 검색된 횟수 표기한다 


## 해결방법
### 0. 공통 응답
- 응답 코드

|code|Description|
|------|---|
|`200`|정상 응답|
|`204`|정상 응답했지만 결과값 없음| 
|`400`|잘못된 요청|
|`404`|잘못된 리소스|
|`500`|서버 오류|

- 정상 응답 공통

|contents|Description|
|------|---|
|`code`|응답 코드|
|`message`|응답 메시지| 
|`data`|API별 응답 내용|

```json
{
  "code": "200",
  "message": "정상 처리",
  "body": {
    ...
  }
}
```

- 오류 응답 공통

|contents|Description|
|------|---|
|`httpStatus`|오류|
|`code`|오류 코드|
|`message`|오류 메시지|

```json
{
  "httpStatus": "BAD_REQUEST",
  "code": 400,
  "message": "파라미터 값을 확인해주세요."
}
```

- 오류 코드

| Exception | Code | Message |
| --- | :---: | --- |
|ApiException | 400 | 검색어를 확인해주세요.|
| |404| 잘못된 URL입니다.|
| |500 |서버 에러입니다. 서버 팀에 연락주세요!|
|MissingServletRequestParameterException|400|파라미터 명을 확인해 주세요.|
|MethodArgumentTypeMismatchException|400|필수 파라미터값이 유효하지 않습니다.|


### 1. 블로그 검색 API
- Request
```
  GET /search HTTP/1.1
  Host : http://localhost:8080/
```
|Parameter|Type|Required|Description|
|------|---|:---:|---|
|keyword|string| O | 검색 키워드|
|page|int| X | 현재 페이지 번호, 기본값 1|
|size|int| X | 한 페이지에 보여질 문서 수, 기본값 10
|sort|string| X | 정렬 방식, accuracy(정확도순), recency(최신순), 기본값 accuracy|

 - Response

   `contnet`

    |Value|Type|Description|
    |:------|:---|:---|
    |title|string|블로그 글 제목|
    |contents|string|블로그 글 요약|
    |url|string|블로그 글 url|
    |blogname|string|블로그 이름|
    |thumbnail|string|미리보기 이미지 URL|
    |datetime|Datetime|블로그 글 작성 시간| 

   `pageable`
  
    |Value|Type|Description|
    |------|---|---|
    |sort|object|정렬 상세정보|
    |pageNumber|integer|현재 페이지 번호|
    |pageSize|integer|한 화면에 보여지는 항목 수|
    |offset|integer|시작하는 위치의 수|
    |paged|boolean|페이징 여부|
    |unpaged|boolean|페이징이 아닌지의 여부| 

   `sort`

    |Value|Type|Description|
    |------|---|---|
    |unsorted|boolean|비정렬 여부|
    |sorted|boolean|정렬 여부|
    |empty|boolean|비어있는지 여부|

   `page`

    |Value|Type|Description|
    |------|---|---|
    |totalPages|integer|전체 페이지수|
    |totalElements|integer|전체 항목수|
    |numberOfElements|integer|실제 데이터 항목 수| 
    |size|integer|페에지 당 항목 수| 
    |last|boolean|마지막 페이지 여부|
    |first|boolean|첫페이지 여부| 
    |number|integer|현재 페이지 번호| 
    |empty|boolean|비어있는지 여부|
    |sort|object|정렬 상세정보| 

`응답 예시`
```json
{
    "status": "OK",
    "message": "정상 처리",
    "data": {
        "content": [
            {
                "title": "다이어트김밥 두부면,<b>닭</b><b>가슴</b><b>살</b>로 만든 김밥",
                "contents": "비율을 좀 더 늘려서 섭취하는데요. 두부가 단백질의 역할을 잘해주고 밥대신 포만감을 주어 든든해요 재료 김밥김1장 상추2장~4장 두부면1포 파프리카 냉동<b>닭</b><b>가슴</b><b>살</b> 100g 집에 있는 냉털 재료들이에요 채소는 파프리카를 넣어도 되구요 당근을 채 썰어 넣거나 오이를 채썰어도 좋아요 아보카도도 무척 잘 어울리구요...",
                "url": "http://mitheduck.tistory.com/54",
                "blogname": "미더덕의 Daily Diary",
                "thumbnail": "https://search4.kakaocdn.net/argon/130x130_85_c/EogldJJpuYt",
                "datetime": "2023-03-20T07:55:26.000+00:00"
            }
          ... 중략
        ],
        "pageable": {
            "sort": {
                "unsorted": false,
                "sorted": true,
                "empty": false
            },
            "pageNumber": 0,
            "pageSize": 10,
            "offset": 0,
            "paged": true,
            "unpaged": false
        },
        "totalPages": 1,
        "totalElements": 10,
        "last": true,
        "numberOfElements": 10,
        "size": 10,
        "sort": {
            "unsorted": false,
            "sorted": true,
            "empty": false
        },
        "number": 0,
        "first": true,
        "empty": false
    }
}
```

- 검색어를 받아 카카오 블로그 검색 API를 호출하는 컨트롤러 구현
  - `@RequestParam("keyword") @NotNull String keyword`를 사용해 파라미터 값 유효성 검사 동시에 실시
  - `@PageableDefault(page = 1, size = 10, sort="accuracy") Pageable pageable`로 page 파라터를 입력하지 않아도 기본값 세팅 
  - 공통 `ApiException`을 작성하여 특정 위치에서 발생하는 에러를 알기 쉽게 에러 핸들링
  - API 호출시 RestTemplate 대신 WebClient 라이브러리 사용
    - Spring 5.0 이상 RestTemplate Depreciated
    - 동시 사용자가 늘수록 RestTemplate의 성능저하
    - [참고링크](https://happycloud-lee.tistory.com/220) 
  - baseUrl과 secretKey는 `application.yaml`에 작성해 key의 보안상 문제 해결
  - 검색 API 호출할 때마다 검색어 기록 DB에 검색어 저장 
    - 동시에 같은 검색어 저장이 일어날 경우 `SearchHistory`의 비스니스 로직인 `increaseCount()` 메서드 호출시 `findByKeyword()` 조회가 동시에 발생
    - `@Lock(LockModeType.PESSIMISTIC_WRITE)` 비관적 락을 통해 검색어 접근 조치
    - [참고링크](https://velog.io/@jkijki12/%EB%8F%99%EC%8B%9C%EC%84%B1-%EC%9D%B4%EC%8A%88-%EB%8F%99%EC%8B%9C%EC%84%B1-%EC%9D%B4%EC%8A%88-%ED%95%B4%EA%B2%B0%ED%95%B4%EB%B3%B4%EA%B8%B0) 
  - PageImpl 구현체를 사용하여 pagination 
  


### 2. 인기 검색어 조회 API
- Request
```
  GET /topKeyword HTTP/1.1
  Host : http://localhost:8080/
```

- Response

  |Value|Type|Description|
  |:------|:---|:---|
  |keyword|string|검색어|
  |searchCnt|integer|검색어 검색 횟수|

`응답 예시`
```json
{
    "status": "OK",
    "message": "정상 처리",
    "data": [
        {
            "keyword": "인기검색어D",
            "searchCnt": 234
        },
        {
            "keyword": "인기검색어J",
            "searchCnt": 223
        },
        {
            "keyword": "인기검색어I",
            "searchCnt": 132
        },
        {
            "keyword": "인기검색어H",
            "searchCnt": 78
        },
        {
            "keyword": "인기검색어C",
            "searchCnt": 44
        },
        {
            "keyword": "인기검색어G",
            "searchCnt": 40
        },
        {
            "keyword": "인기검색어B",
            "searchCnt": 23
        },
        {
            "keyword": "인기검색어E",
            "searchCnt": 11
        },
        {
            "keyword": "인기검색어A",
            "searchCnt": 10
        },
        {
            "keyword": "인기검색어F",
            "searchCnt": 4
        }
    ]
}
```
- `JpaRepository`에서 `@Query` 어노테이션을 통해 **Native Query**를 사용
  - limit이 필요하고 10위까지의 순위는 변동되지 않기에 따로 파라미터로 두지 않음
- Query의 결과는 `KeywordResponse` Dto에 담아서 `List<>` 형태로 반환
- 반환 내용이 없을 경우 `NO_DATA` 에러가 발생
