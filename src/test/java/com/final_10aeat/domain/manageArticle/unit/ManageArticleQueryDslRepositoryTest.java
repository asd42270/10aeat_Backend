package com.final_10aeat.domain.manageArticle.unit;

import static com.final_10aeat.common.util.EntityUtil.DEFAULT_OFFICE;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.final_10aeat.common.config.TestConfig;
import com.final_10aeat.common.enumclass.ManagePeriod;
import com.final_10aeat.common.enumclass.Progress;
import com.final_10aeat.domain.manageArticle.entity.ManageArticle;
import com.final_10aeat.domain.manageArticle.repository.ManageArticleRepository;
import com.final_10aeat.domain.office.entity.Office;
import com.final_10aeat.domain.office.repository.OfficeRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@DataJpaTest
@Transactional
@Import(TestConfig.class)
public class ManageArticleQueryDslRepositoryTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ManageArticleRepository manageArticleRepository;
    @Autowired
    private OfficeRepository officeRepository;
    private final Office office = DEFAULT_OFFICE;

    @Test
    @DisplayName("건물id와 텍스트로 검색 테스트")
    void searchByOfficeIdAndTextTest() {
        Office savedOffice = officeRepository.save(office);
        List<ManageArticle> manageArticles = List.of(
            makeArticle("none", savedOffice),
            makeArticle("none", savedOffice),
            makeArticle("none", savedOffice),
            makeArticle("none", savedOffice),
            makeArticle("keyword", savedOffice)
        );

        manageArticleRepository.saveAll(manageArticles);
        PageRequest pageRequest = PageRequest.of(0, 3);

        Page<ManageArticle> keyword = manageArticleRepository
            .searchByOfficeIdAndText(savedOffice.getId(), "keyword", pageRequest);

        assertEquals(keyword.getContent().size(), 1);
    }

    ManageArticle makeArticle(String title, Office office) {
        return ManageArticle.builder()
            .period(ManagePeriod.YEAR)
            .progress(Progress.PENDING)
            .title(title)
            .office(office)
            .build();
    }
}
