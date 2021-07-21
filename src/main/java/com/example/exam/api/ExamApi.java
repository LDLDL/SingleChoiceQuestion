package com.example.exam.api;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.exam.bean.ExamMap;
import com.example.exam.bean.QuestionNoAnswer;
import com.example.exam.entity.Admin;
import com.example.exam.entity.Question;
import com.example.exam.entity.QuestionId;
import com.example.exam.entity.Student;
import com.example.exam.repository.AdminRepository;
import com.example.exam.repository.QuestionRepository;
import com.example.exam.repository.StudentRepository;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class ExamApi {
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private ExamMap examMap;

    private final String jwtHmacKey = "ThisIsTestKey";

/*
    @GetMapping("/admins")
    public HttpEntity<?> getAdmins() {
        return new ResponseEntity<>(adminRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/students")
    public HttpEntity<?> getStudents() {
        return new ResponseEntity<>(studentRepository.findAll(), HttpStatus.OK);
    }
*/


    @PostMapping("/students/register")
    public HttpEntity<?> addStudent(@RequestBody Student stu) {
        return new ResponseEntity<>(studentRepository.save(stu), HttpStatus.CREATED);
    }

    @PostMapping("/admins")
    public HttpEntity<?> addAdmin(@RequestBody Admin admin) {
        return new ResponseEntity<>(adminRepository.save(admin), HttpStatus.CREATED);
    }

    @GetMapping("/exams/generation/{n}")
    public HttpEntity<?> getExam(@PathVariable int n) {
        Long examId = System.currentTimeMillis();
        StringBuilder answerBuilder = new StringBuilder();
        List<QuestionNoAnswer> exam = new ArrayList<>(n);

        List<Question> questions = questionRepository.getExam(n);

        for (Question qi : questions) {
            exam.add(new QuestionNoAnswer(qi));
            answerBuilder.append(qi.getAnswer_id());
        }

        examMap.putExamAnswer(examId, answerBuilder.toString());

        return new ResponseEntity<>(
                Map.of("id", examId, "data", exam),
                HttpStatus.OK
        );
    }

    @PostMapping("/exams/check/{id}")
    public HttpEntity<?> checkExam(@PathVariable Long id, @RequestBody List<String> answer) {
        String rightAnswer = examMap.getExamAnswer(id).toUpperCase();
        examMap.deleteExamAnswer(id);
        List<String> rightAnswerList = new ArrayList<>();

        for (int i = 0; i < rightAnswer.length(); ++i) {
            rightAnswerList.add(String.valueOf(rightAnswer.charAt(i)));
        }

        int questionCount = rightAnswer.length();
        int rightCount = 0;
        for (int i = 0; i < answer.size(); ++i) {
            if (answer.get(i).toUpperCase().charAt(0) == rightAnswerList.get(i).charAt(0)) {
                rightCount++;
            }
        }
        return new ResponseEntity<>(
                Map.of(
                        "rightCount", rightCount,
                        "totalCount", questionCount,
                        "rightAnswer", rightAnswerList,
                        "userAnswer", answer
                ),
                HttpStatus.OK
        );
    }

    @GetMapping("/questions")
    public HttpEntity<?> getQuestions() {
        return new ResponseEntity<>(questionRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/questions/pages/{n}")
    public HttpEntity<?> getQuestionsPageN(@PathVariable int n) {
        Pageable pageable = PageRequest.of(n - 1, 10);
        return new ResponseEntity<>(questionRepository.findAll(pageable), HttpStatus.OK);
    }

    @PostMapping("/questions")
    public HttpEntity<?> addQuestion(@RequestBody JSONObject qj) {
        Question question = new Question(
                qj.getAsString("question"),
                qj.getAsString("A"),
                qj.getAsString("B"),
                qj.getAsString("C"),
                qj.getAsString("D"),
                qj.getAsString("answer"),
                qj.getAsString("answer_id")
        );
        return new ResponseEntity<>(questionRepository.save(question), HttpStatus.CREATED);
    }

    @DeleteMapping("/questions")
    public HttpEntity<?> deleteQuestion(@RequestBody JSONObject json) {
        String dq = json.getAsString("question");
        System.out.println(dq);
        questionRepository.deleteByQuestion(new QuestionId(dq));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/login/students")
    public HttpEntity<?> loginStudents(@RequestBody JSONObject json) {
        String stuName = json.getAsString("name");
        String stuPwd = json.getAsString("password");

        Student s = studentRepository.findStudentByNameAndPwd(stuName, stuPwd);
        if (s == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Calendar nowTime = Calendar.getInstance();
        nowTime.add(Calendar.MINUTE, 30);
        Date expiresDate = nowTime.getTime();
        String token = JWT.create().withAudience(s.getId().toString())
                .withIssuedAt(new Date())
                .withExpiresAt(expiresDate)
                .withClaim("name", s.getName())
                .sign(Algorithm.HMAC256(jwtHmacKey));

        return new ResponseEntity<>(Map.of("token", token), HttpStatus.OK);
    }

    @PostMapping("/login/admins")
    public HttpEntity<?> loginAdmins(@RequestBody JSONObject json) {
        String adminName = json.getAsString("name");
        String adminPwd = json.getAsString("password");

        Admin a = adminRepository.findAdminByNameAndPwd(adminName, adminPwd);
        if (a == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Calendar nowTime = Calendar.getInstance();
        nowTime.add(Calendar.MINUTE, 30);
        Date expiresDate = nowTime.getTime();
        String token = JWT.create().withAudience(a.getId().toString())
                .withIssuedAt(new Date())
                .withExpiresAt(expiresDate)
                .withClaim("name", a.getName())
                .sign(Algorithm.HMAC256(jwtHmacKey));

        return new ResponseEntity<>(Map.of("token", token), HttpStatus.OK);
    }

    @PostMapping("/login/token")
    public HttpEntity<?> loginToken(@RequestBody JSONObject json) {
        String token = json.getAsString("token");

        try{
            String uids = JWT.require(Algorithm.HMAC256(jwtHmacKey))
                    .build()
                    .verify(token)
                    .getAudience()
                    .get(0);
            Long uid = Long.parseLong(uids);
            Optional<Admin> admin = adminRepository.findById(uid);
            Optional<Student> stu = studentRepository.findById(uid);
            if (admin.isPresent()) {
                return new ResponseEntity<>(
                        Map.of("name", admin.get().getName()),
                        HttpStatus.OK
                );
            }
            else if (stu.isPresent()) {
                return new ResponseEntity<>(
                        Map.of("name", stu.get().getName()),
                        HttpStatus.OK
                );
            }
        }
        catch (Exception ignored){

        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
