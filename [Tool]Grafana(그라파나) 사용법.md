# Grafana(그라파나) 사용법

# 목차

## 참고 링크
- URL : https://hstory0208.tistory.com/entry/Grafana-%EA%B7%B8%EB%9D%BC%ED%8C%8C%EB%82%98-%EC%84%A4%EC%B9%98-%EB%B0%A9%EB%B2%95-Window
- URL : https://hstory0208.tistory.com/entry/Grafana-%EA%B7%B8%EB%9D%BC%ED%8C%8C%EB%82%98-%EC%84%A4%EC%B9%98-%EB%B0%A9%EB%B2%95-Window
- 
## 다운로드 링크
- URL : https://grafana.com/grafana/download
- Windows Installer : https://dl.grafana.com/enterprise/release/grafana-enterprise-11.0.0.windows-amd64.msi
- Windows Binaries: https://dl.grafana.com/enterprise/release/grafana-enterprise-11.0.0.windows-amd64.zip
  - 아래의 내용은 Binaries 로 진행함


## 실행
- 경로 : grafana-v11.0.0\bin
- 파일 : grafana-server.exe

## 접속
- URL : http://localhost:3000/
- ID / PW : admin / admin
  - 처음 로그인 시 비밀번호를 변경하라고 나오는데, 스킵도 가능함

## Prometheus(프로메테우스) 연동
1. 좌측의 Connections 클릭
2. Add new connection 클릭
3. Prometheus 검색 후 클릭
4. 우측 상단의 Add new data source 클릭
5. 설정화면에서 아래의 정보 입력
```text
name : 대시보드에서 사용할 이름
connection : 프로메테우스 접속 URL:port
```
6. 정보 입력 후 하단의 Save & test 클릭
7. save 후 좌측 탭 메뉴의 Connections 하위 메뉴에서 Data sources 클릭
8. Prometheus가 추가된것을 확인하고 Build a dashboard 클릭
9. Add visualization 클릭
10. 화면 설정 후 우측 상단의 Save 클릭
11. 대시보드 이름 및 설명 저장
