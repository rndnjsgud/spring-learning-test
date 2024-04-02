package cholog;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class MemberController {

    private final List<Member> members = new ArrayList<>();
    private final AtomicLong index = new AtomicLong(1);

    @PostMapping("/members")
    public ResponseEntity<Void> create(@RequestBody Member member) {
        // TODO: member 정보를 받아서 생성한다.   ->@RequestBody 사용,
        Member newMember = Member.toEntity(member, index.getAndIncrement());
        members.add(newMember);
        return ResponseEntity.created(URI.create("/members/" + newMember.getId())).build(); // URI를 만들어 build(바디 없이 생성) 후 반환
    }

    @GetMapping("/members")
    public ResponseEntity<List<Member>> read() {
        // TODO: 저장된 모든 member 정보를 반환한다.
        return ResponseEntity.ok().body(members);   //ok는 응답, body에 members를 담아 반환
    }

    @PutMapping("/members/{id}")    //수정 시 PutMapping 사용
    public ResponseEntity<Void> update(@RequestBody Member newMember, @PathVariable Long id) {  //RequestBody로 객체 요청, @PathVariable 통해 경로 변수 id 사용
        // TODO: member의 수정 정보와 url 상의 id 정보를 받아 member 정보를 수정한다.
        Member member = members.stream()
            .filter(it -> Objects.equals(it.getId(), id))   // id 동일한 것 찾음
            .findFirst()
            .orElseThrow(RuntimeException::new);

        member.update(newMember);
        return ResponseEntity.ok().build(); //빌드 후 반환
    }

    @DeleteMapping("/members/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        // TODO: url 상의 id 정보를 받아 member를 삭제한다.
        Member member = members.stream()
            .filter(it -> Objects.equals(it.getId(), id))
            .findFirst()
            .orElseThrow(RuntimeException::new);

        members.remove(member);

        return ResponseEntity.noContent().build();  //noContent는 응답, 빌더 패턴
    }
}
