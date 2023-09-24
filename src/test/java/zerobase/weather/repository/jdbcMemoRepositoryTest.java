package zerobase.weather.repository;

import org.junit.jupiter.api.Test;
import zerobase.weather.domain.Memo;
import zerobase.weather.repository.JdbcMemoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
class jdbcMemoRepositoryTest {

    @Autowired
    JdbcMemoRepository jdbcMemoRepository;


    @Test
    void saveTest(){
        //given
        Memo memo=new Memo(1,"안녕");

        //when
        Memo save = jdbcMemoRepository.save(memo);

        //then
        assertEquals(memo.getId(),save.getId());
        assertEquals(memo.getText(),save.getText());
    }

    @Test
    void findAllTest(){
        //given
        Memo memo=new Memo(2,"안녕");
        Memo memo2=new Memo(3,"안녕");
        jdbcMemoRepository.save(memo);
        jdbcMemoRepository.save(memo2);
        //when
        List<Memo> all = jdbcMemoRepository.findAll();

        //then
        System.out.println(all);
        assertNotNull(all);
    }
}