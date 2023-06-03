# capstone_design

[앱 다운 링크-release.apk파일](https://github.com/hyunjunkimAbc/capstone_design/blob/main/android/app/release/app-release.apk)
---
[앱 다운 링크-debug.apk파일](https://github.com/hyunjunkimAbc/capstone_design/blob/main/android/app/debug/app-debug.apk)
---
상세내용(실행화면과 구조)은 아래 링크를 참고해주세요.
---
[결과보고서.pdf](https://github.com/hyunjunkimAbc/capstone_design/blob/main/doc/%5BUNITE%5D%EC%8B%A4%ED%96%89%ED%99%94%EB%A9%B4%EA%B3%BC_%EA%B5%AC%EC%A1%B0.pdf)
<br/><br/>
[작품구조](https://github.com/hyunjunkimAbc/capstone_design/blob/main/doc/%EC%9E%91%ED%92%88%EA%B5%AC%EC%A1%B0.png)
<br/><br/>
[요구사항 명세서](https://github.com/hyunjunkimAbc/capstone_design/blob/main/doc/%EC%9A%94%EA%B5%AC%EC%82%AC%ED%95%AD_%EB%AA%85%EC%84%B8%EC%84%9C_ver2.pdf)
***
팀 번호: 54팀
팀 명:UNITE
프로젝트 명: 번개모임(AI Meeting Concierge)

팀명 : UNITE
지도교수 : 이동희 교수님
팀원 : 김현준(팀장)
안재웅
김동경
김범학

-작품 개요 : 관심 카테고리 별 모임 매칭 및 관리
MAP API 기반 모임추천 및 감정분석
AI모델로 대여장소와 각종대회를 추천하는
모임 컨시어지(비서) 앱

-작품 설명:
1인 가구 수가 증가하고 있는 추세에 사람들의 우울감을 해소시키위해 정기적인 모임을 개설하고 관리하는 서비스 제공한다. 모임은 관심 카테고리를 설정하여 이용자가 관심있을만한 모임을 우선으로 보여준다.
번개모임은 단기적인 모임으로 타지역에 갔을 때나 급하게 다수의 인원을 필요로 할 때 이용할 수 있다. 활동에필요한 인원을
MAP API
를 기반으로 나의 위치에서 가까운 모임을 즉흥적으로 매칭시켜주는 서비스이다.
모임활동에 필요한 장소를 대여할 수 있는 장소대여 예약 서비스를
감정분석 AI모델
을 통해 제공하고 모임활동에 관련된 각종 대회들을 보여주고 참가 신청도 할 수 있는 대회 서비스도 제공한다.
이 서비스를 이용한 사용자들이 입력한 리뷰를 가지고 AI 감정분석하여 선호도가 높은 모임, 장소, 대회를 추천한다. 즉, 모임활동에 필요한 기능을 모아 앱 하나로 관리할 수 있는 종합적인
모임 컨시어지
(비서) 안드로이드앱 서비스이다


기대효과:
1. 같은 관심사를 가진 사람들을 매칭하여 공동체 의식과 소속감을 형성하고
   우울감을 해소
2. 다수의 인원이 필요한 상황에 필요한 인원을
   즉흥적으로 매칭하여
   빠르게 문제 해결
3. AI 감정분석을 통해 선호도가 높은 컨텐츠를 추천
   함으로써 모임 활동의 편의성 증가

타겟 머신 : Android
개발 환경 : Window
개발 도구 : Android Studio
개발 언어: kotlin
운영체제 : Android
서버(백엔드): Google Firebase
API : NAVER MAP api, poi 검색 api(T맵),네이버 파파고
감정 분석 ai 모델 :TensorFlow-Starter Model

감정분석 ai 모델 링크:
https://www.tensorflow.org/lite/examples/text_classification/overview?hl=ko
https://storage.googleapis.com/download.tensorflow.org/models/tflite/text_classification/text_classification_v2.tflite

참고자료:
https://www.tensorflow.org/lite/inference_with_metadata/task_library/nl_classifier?hl=ko
https://developers.naver.com/docs/papago/papago-nmt-example-code.md
