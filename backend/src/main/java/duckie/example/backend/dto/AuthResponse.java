package duckie.example.backend.dto;

public record  AuthResponse (
    String accessToken,
    String refreshToken,
    String tokenType,
    Long expiresInSecond,
    UserResponse user
){
    public AuthResponse{
        if(tokenType==null || tokenType.isBlank()){
            tokenType="Bearer";
        }
    }

    public static AuthResponse of(
        String accessToken,
        String refreshToken,
        String tokenType,
        Long expiresInSecond,
        UserResponse user
    ){
        return new AuthResponse(accessToken,refreshToken,tokenType,expiresInSecond,user);
    }
}