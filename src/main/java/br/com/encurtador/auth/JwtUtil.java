package br.com.encurtador.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

/**
 * Utilitário simples para gerar JWT.
 * OBS: Isso é apenas para demonstração (entrevista).
 */
public class JwtUtil {

    // Chave fixa só para demo (em produção seria env/secret manager)
    private static final String SECRET = "ENCURTADOR_DEMO_SECRET_1234567890";
    private static final long EXPIRACAO_MS = 60 * 60 * 1000; // 1 hora

    /**
     * Gera um JWT simples contendo o "user" como subject.
     */
    public static String gerarToken(String user) {

        // --- Cria uma chave HMAC com base no SECRET ---
        byte[] secretBytes = SECRET.getBytes(StandardCharsets.UTF_8);
        Key key = new SecretKeySpec(secretBytes, SignatureAlgorithm.HS256.getJcaName());

        // --- Datas (agora e expiração) ---
        Date agora = new Date();
        Date expiraEm = new Date(agora.getTime() + EXPIRACAO_MS);

        // --- Monta e assina o token ---
        String token =
                Jwts.builder()
                        .setSubject(user)
                        .setIssuedAt(agora)
                        .setExpiration(expiraEm)
                        .signWith(key)
                        .compact();

        return token;
    }
}
