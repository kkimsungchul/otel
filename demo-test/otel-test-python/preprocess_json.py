import pandas as pd
import json
path = '/Users/jihyoun/Otel/'
log = open('example.log')

def preprocess_json(input_file, output_file):
    # 파일을 읽고 개별 JSON 객체를 리스트에 추가
    objects = []
    with open(input_file, 'r') as file:
        for line in file:
            if line.strip():  # 빈 줄이 아닌 경우에만 처리
                objects.append(json.loads(line))

    # 모든 객체를 하나의 JSON 배열로 저장
    with open(output_file, 'w') as file:
        json.dump(objects, file, indent=4)  # 보기 좋게 들여쓰기 추가

# 파일 경로 설정
input_file_path = 'example.json'
output_file_path = 'output.json'

# 함수 호출
preprocess_json(input_file_path, output_file_path)
