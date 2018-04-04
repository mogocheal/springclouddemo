使用 OAuth 2.0 访问资源
-----------------

### 使用 curl

1. 获取 access token
   ```
   curl apiclient:secret@localhost:8080/oauth/token -d grant_type=password -d username=cici -d password=123456
   ```
   返回
   ```
   {"access_token":"2b9b484a-4cd7-4bb4-aa6d-d6b3550c4334","token_type":"bearer","refresh_token":"fae2651e-f32c-48ec-87ca-6244b17d96df","expires_in":3598,"scope":"read","organization":"cici"}
   ```

2. 出示 access token 访问资源信息
   ```
   curl --header "Authorization: Bearer 2b9b484a-4cd7-4bb4-aa6d-d6b3550c4334" http://localhost:8000/api/station/greeting
   ```
   返回
   ```
   {"id":1,"content":"Hello, World!"}
   ```

使用 OAuth2RestTemplate

```Java
@Autowired
private OAuth2ClientContext oauth2Context;

@Bean
public OAuth2RestTemplate sparklrRestTemplate() {
    return new OAuth2RestTemplate(sparklr(), oauth2Context);
}
```