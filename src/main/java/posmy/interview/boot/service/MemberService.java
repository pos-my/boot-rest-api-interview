package posmy.interview.boot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import posmy.interview.boot.model.Member;
import posmy.interview.boot.repository.MemberRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class MemberService {

    @Autowired
    MemberRepository memberRepository;

    //getting all Member record by using the method findaAll() 
    public List<Member> getAllMember()
    {
        List<Member> Member = new ArrayList<Member>();
        memberRepository.findAll().forEach(Member1 -> Member.add(Member1));
        return Member;
    }

    //getting a specific record by using the method findById()
    public Member getMemberById(int id)
    {
        return memberRepository.findById(id).get();
    }

    //saving a specific record by using the method save()
    public void saveOrUpdate(Member Member)
    {
        memberRepository.save(Member);
    }

    //deleting a specific record by using the method deleteById()
    public void delete(int id)
    {
        memberRepository.deleteById(id);
    }

    //updating a record
    public void update(Member Member, int memberid)
    {
        memberRepository.save(Member);
    }
}
