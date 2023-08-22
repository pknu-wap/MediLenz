![image](https://user-images.githubusercontent.com/48265129/236689315-647e33d2-056e-4a03-91d7-a8c985032daa.png)

Medilenz는 사용자가 필요로 하는 의약품 정보를 검색하고, 사용자들끼리 의약품에 관한 정보를 주고받을 수 있는 커뮤니티 기능을 제공하는 Android Native 앱입니다.

## 주요 기능

1. **의약품 검색 기능**: 의약품명/제조사명 등 사용자가 직접 입력하여 검색, 카메라로 의약품을 검출하여 분류해주는 AI 검색
2. **댓글**: 한 의약품에 대해서 사용자들간 대화
3. **관심 의약품 목록 관리**: 내가 관심있는 의약품 목록 추가하여 관리 가능
4. **내 댓글 목록**
5. **의약품 관련 공고 및 뉴스**: 의약품 제조사에 대한 행정 처분, 의약품 회수 폐기와 같은 공고 또는 뉴스를 볼 수 있음
6. **약물간 상호작용 확인**
7. **의약품 안전 사용 정보 확인**
8. **의약품 상세 정보**: 효능효과, 용법 용량, 주의 사항, 식별 정보 등 자세한 정보를 볼 수 있음

## 사용 기술 및 데이터

- **앱 개발**: Android Studio
- **언어**: Kotlin, Java, Python
- **앱 화면 제작**: XML(대부분), Compose(일부)
- **백엔드**: node.js, Google Vertex AI, AWS RDS(SQL)
- **딥러닝 모델**: yolov8, google auto ml, or efficient net (이미지로 약 분류)
- **의약품 정보 데이터**: 식약처 데이터(OpenApi)
- **의약품 AI 학습 데이터**: AI Hub(과기부) 데이터

### Android 사용 스택

- databinding
- viewbinding
- viewpager2
- paging3
- retorfit2
- glide
- hilt
- kotlinx-serialization
- datastore
- 그 외 jetpack library

### Android 앱 아키텍처

- MVVM
- domain 영역 포함

## Modules

![image](https://user-images.githubusercontent.com/48265129/234879804-42a22684-4534-421f-b71f-c83122e8e613.png)

## Members
 #### 아래의 프로필 이미지를 클릭하시면 저희 팀원들의 커밋 내역을 확인하실 수 있습니다! 😀
| [@ho-sick99](https://github.com/ho-sick99) | [@pknujsp](https://github.com/pknujsp) | [@tgyuuAn](https://github.com/tgyuuAn) | [@winocreative](https://github.com/winocreative) |
| :---: | :---: | :---: | :---: |
| <a href="https://github.com/pknu-wap/2023_1_WAP_APP_TEAM_MEDI/commits/server_develop?author=ho-sick99"><img src="https://avatars.githubusercontent.com/u/83945722?s=64&v=4" width="64" height="64"></a> | <a href="https://github.com/pknu-wap/2023_1_WAP_APP_TEAM_MEDI/commits/android_develop?author=pknujsp"><img src="https://avatars.githubusercontent.com/u/48265129?s=64&v=4" width="64" height="64"></a> | <a href="https://github.com/pknu-wap/2023_1_WAP_APP_TEAM_MEDI/commits/android_develop?author=tgyuuAn"><img src="https://avatars.githubusercontent.com/u/116813010?s=64&v=4" width="64" height="64"></a> | <a href="https://github.com/pknu-wap/2023_1_WAP_APP_TEAM_MEDI/commits/server_develop?author=winocreative"><img src="https://avatars.githubusercontent.com/u/26576118?s=64&v=4" width="64" height="64"></a> |


## Git Flow

기본적으로 Git Flow 전략을 이용한다. 작업 시작 시 선행되어야 할 작업은 다음과 같다.

1. Issue를 생성한다.
2. feature Branch를 생성한다.
3. Add - Commit - Push - Pull Request 의 과정을 거친다.
4. Pull Request가 작성되면 작성자 이외의 다른 팀원이 Code Review를 한다.
5. Code Review가 완료되면 Pull Request 작성자가 develop Branch로 merge 한다.
6. merge된 작업이 있을 경우, 다른 브랜치에서 작업을 진행 중이던 개발자는 본인의 브랜치로 merge된 작업을 Pull 받아온다.
7. 종료된 Issue와 Pull Request의 Label과 Project를 관리한다.`

## App Design Templates (Figma)

![image](https://github.com/pknu-wap/2023_1_WAP_APP_TEAM_MEDI/assets/116813010/466463b2-b69e-40fe-bcb2-30685a4cf7c0)


<https://www.figma.com/file/wgaIpwcuWfSpy1SA52wiUt/%ED%99%94%EB%A9%B4?node-id=0%3A1&t=VEl3ZiT2Q5Cyp4ps-1>
