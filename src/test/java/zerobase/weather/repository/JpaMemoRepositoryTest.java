package zerobase.weather.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import zerobase.weather.domain.Memo;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class JpaMemoRepositoryTest {

    @Autowired
    JpaMemoRepository jpaMemoRepository;

    @Test
    void saveTest(){
        //given
        Memo memo=new Memo(10,"ㅇㅇㅇ" );
        //when
        jpaMemoRepository.save(memo);
        //then

        List<Memo> all = jpaMemoRepository.findAll();
        assertTrue(all.size()>0);
    }

    @Test
    void finfById(){
        //given
        Memo memo=new Memo(10,"ㅇㅇㅇ" );
        //when
        Memo result = jpaMemoRepository.save(memo);
        //then
        Optional<Memo> byId = jpaMemoRepository.findById(result.getId());

        assertEquals(byId.get().getId(),result.getId());
    }
}