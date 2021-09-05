# KPUCountDown

## Introduction

코로나19 백신 접종 현황 / 산기대 대면 예상일을 확인할 수 있는 API

## Demo

[URL](http://13.125.8.153:8080)

## API Docs

[Swagger](http://13.125.8.153:8080/swagger-ui/)

## Tech Stacks

* Spring Boot
* Spring MVC
* Spring Data JPA
* H2 Database
* Deeplearning4j
* Docker / Docker Compose

## Inference

### Model Structure

* Xavier 초기화 사용
* SGD 최적화 함수
* Adam 업데이트 함수
* MSE 손실 함수
* 1x128 Relu
* 128x128 Sigmoid
* 128x128 Cube
* 128x128 Sigmoid
* 128x128 Softplus
* 128x128 Cube
* 128x1 Identity 

### Evaluation Summary

![expectation](https://user-images.githubusercontent.com/32592965/132117091-6a813d15-7c98-4590-841d-d0e3b5c2eef0.png)

기존 데이터와 통합한 추론 그래프

![raw expectation](https://user-images.githubusercontent.com/32592965/132117093-ec21032c-cdb9-4759-95fb-e1d1fc42ef8e.png)

학습된 Raw 추론 그래프
