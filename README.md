# spring_2025_Evstafev

## Load Testing
Использовал [Fortio](https://github.com/fortio/fortio?tab=readme-ov-file)

Добавил одно значение в таблицу перед тестированием. Будем ломиться в него GET запросами. 

Settings: 
- GET
- URL: http://localhost:8080/api/v1/get?customerId=1
- QPS: 100'000
- Duration: 20s
- Threads/Simultaneous connections: \{4, 40, 4\}

В таблице представлены результаты НТ: qps от Threads для 2-ух реализаций.

| Threads | HashMap    | PostgreSQL  |
|---------|------------|-------------|
| 4       | 25554.2    | 8614.9      |
| 8       | 40684.6    | 14377.9     |
| 12      | 51261.7    | 17814.7     |
| 16      | 53684.0    | 18854.7     |
| 20      | 55736.2    | 19628.6     |
| 24      | 57401.1    | 19854.2     |
| 28      | 58747.1    | 19871.3     |
| 32      | 60135.2    | 19900.8     |
| 36      | 60525.7    | 19604.7     |
| 40      | 60856.7    | 19931.9     |

