# spring_2025_Evstafev

## Load Testing
Использовал [Fortio](https://github.com/fortio/fortio?tab=readme-ov-file)

Добавил одно значение в HashTable перед тестированием. Будем ломиться в него GET запросами. 

Test case: 1

Settings: 
- GET
- URL: http://localhost:8080/api/v1/get?customerId=1
- QPS: 3000
- Duration: 10s
- Threads/Simultaneous connections: 10

Results: [res1](https://github.com/ArtEvst/spring_2025_Evstafev/blob/hmw1/customerservice/results/1.png)


Test case: 2

Settings: 
- GET
- URL: http://localhost:8080/api/v1/get?customerId=1
- QPS: 10000
- Duration: 10s
- Threads/Simultaneous connections: 10

Results: [res2](https://github.com/ArtEvst/spring_2025_Evstafev/blob/hmw1/customerservice/results/2.png)

