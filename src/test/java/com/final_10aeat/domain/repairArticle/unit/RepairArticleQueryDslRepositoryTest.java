package com.final_10aeat.domain.repairArticle.unit;

import static com.final_10aeat.common.util.EntityUtil.DEFAULT_OFFICE;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.final_10aeat.common.config.TestConfig;
import com.final_10aeat.common.enumclass.ArticleCategory;
import com.final_10aeat.common.enumclass.Progress;
import com.final_10aeat.domain.office.entity.Office;
import com.final_10aeat.domain.office.repository.OfficeRepository;
import com.final_10aeat.domain.repairArticle.entity.RepairArticle;
import com.final_10aeat.domain.repairArticle.repository.RepairArticleRepository;
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
public class RepairArticleQueryDslRepositoryTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private RepairArticleRepository repairArticleRepository;
    @Autowired
    private OfficeRepository officeRepository;
    private final Office office = DEFAULT_OFFICE;

    @Test
    @DisplayName("건물id와 텍스트로 검색 테스트")
    void searchByOfficeIdAndTextTest() {
        // given
        Office savedOffice = officeRepository.save(office);
        List<RepairArticle> articles = List.of(
            makeArticle("none", savedOffice),
            makeArticle("none", savedOffice),
            makeArticle("none", savedOffice),
            makeArticle("none", savedOffice),
            makeArticle("keyword", savedOffice)
        );

        repairArticleRepository.saveAll(articles);
        PageRequest pageRequest = PageRequest.of(0, 3);

        // when
        Page<RepairArticle> keyword = repairArticleRepository
            .searchByTextAnsOfficeId(savedOffice.getId(), "keyword", pageRequest);

        // then
        assertEquals(keyword.getContent().size(), 1);
    }

    RepairArticle makeArticle(String title, Office office) {
        return RepairArticle.builder()
            .progress(Progress.INPROGRESS)
            .category(ArticleCategory.REPAIR)
            .title(title)
            .viewCount(0)
            .office(office)
            .build();
    }
}
