package com.sosim.server.jwt;

import com.sosim.server.jwt.dao.JwtDao;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class JwtRepositoryImpl implements JwtRepository{

    @Override
    public Optional<JwtDao> findByRefreshToken(String refreshToken) {
        return Optional.empty();
    }

    @Override
    public <S extends JwtDao> S save(S entity) {
        return null;
    }

    @Override
    public <S extends JwtDao> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<JwtDao> findById(String s) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(String s) {
        return false;
    }

    @Override
    public Iterable<JwtDao> findAll() {
        return null;
    }

    @Override
    public Iterable<JwtDao> findAllById(Iterable<String> strings) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(String s) {

    }

    @Override
    public void delete(JwtDao entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends String> strings) {

    }

    @Override
    public void deleteAll(Iterable<? extends JwtDao> entities) {

    }

    @Override
    public void deleteAll() {

    }
}
