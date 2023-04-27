## Architecture

View-Model-Domain(UseCase)-Repository-Data(Local/Remote)

## Library

retrofit2   
kotlinx-serialization   
room   
glide   
workmanager   
datastore   
hilt   
compose

## Modules

![image](https://user-images.githubusercontent.com/48265129/234879804-42a22684-4534-421f-b71f-c83122e8e613.png)

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

<https://www.figma.com/file/wgaIpwcuWfSpy1SA52wiUt/%ED%99%94%EB%A9%B4?node-id=0%3A1&t=VEl3ZiT2Q5Cyp4ps-1>
