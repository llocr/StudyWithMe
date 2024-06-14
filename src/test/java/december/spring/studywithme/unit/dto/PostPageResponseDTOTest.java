package december.spring.studywithme.unit.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import december.spring.studywithme.dto.PostPageResponseDTO;
import december.spring.studywithme.entity.Post;
import december.spring.studywithme.entity.User;
import december.spring.studywithme.utils.MonkeyUtils;

class PostPageResponseDTOTest {
	@Test
	@DisplayName("PostPageResponseDTO 생성 테스트")
	void DTO생성() {
		//given
		User user = MonkeyUtils.commonMonkey().giveMeOne(User.class);
		Post post1 = Post.builder().user(user).title("title").contents("content").build();
		Post post2 = Post.builder().user(user).title("title").contents("content").build();
		
		List<Post> posts = Arrays.asList(post1, post2);
		
		Page<Post> postPage = new PageImpl<>(posts, PageRequest.of(0, 10, Sort.by(Sort.Order.asc("createdAt"))), posts.size());
		int currentPage = 1;

	    //when
		PostPageResponseDTO responseDTO = new PostPageResponseDTO(currentPage, postPage);

	    //then
		assertThat(responseDTO.getCurrentPage()).isEqualTo(currentPage);
		assertThat(responseDTO.getTotalElements()).isEqualTo(postPage.getTotalElements());
		assertThat(responseDTO.getTotalPages()).isEqualTo(postPage.getTotalPages());
		assertThat(responseDTO.getSize()).isEqualTo(postPage.getSize());
		assertThat(responseDTO.getSortBy()).isEqualTo(postPage.getSort().toString());
	}
}