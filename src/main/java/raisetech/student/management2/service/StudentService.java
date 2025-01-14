package raisetech.student.management2.service;

import static java.time.LocalDate.now;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.student.management2.data.StudentsCourses;
import raisetech.student.management2.data.Student;
import raisetech.student.management2.domain.StudentDetail;
import raisetech.student.management2.repositiry.StudentRepository;

/**
 * 受講生情報を取り扱うサービス
 * 検索や・登録更新処理を行う
 */
@Service
public class StudentService {

  private StudentRepository repository;

  @Autowired
  public StudentService(StudentRepository repository) {
    this.repository = repository;
  }

  /**
   * 受講生一覧検索
   *  *全件検索の為、条件指定は行いません。
   * @return 受講生一覧(全件)
   */
  public List<Student> searchStudentList() {
 //   レッスン24：return repository.search().stream()
 //           .filter(student -> student.getAge() > 30).collect(Collectors.toList());
    return repository.search();
  }

  /**
   * 受講生検索
   * IDに紐づく受講生情報を主t九したあと、その受講生に紐づく受講生コース情報を取得し設定
   * @param id 受講生ID
   * @return 受講生
   */
  public  StudentDetail searchStudent(String id){

    Student student = repository.searchStudent(id);
    List<StudentsCourses> studentsCourses = repository.searchStudentCourses(student.getId());
    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(student);
    studentDetail.setStudentsCourses(studentsCourses);
    return studentDetail;
  }
//サーチ処理を行う（コントローラ）

  public List<StudentsCourses> searchCourseList() {
    return repository.searchStudentCoursesList();

  }


  @Transactional
  public StudentDetail registerStudent(StudentDetail studentDetail){
    repository.registerStudent(studentDetail.getStudent());
    for (StudentsCourses studentsCourse : studentDetail.getStudentsCourses()) {
      studentsCourse.setStudentId(studentDetail.getStudent().getId());
      studentsCourse.setCourseStartAt(Date.valueOf(now()));
      studentsCourse.setCourseEndAt(Date.valueOf(now().plusYears(1)));

      repository.registerStudentsCourses(studentsCourse);
    }
    return studentDetail;
  }
//TODO: コース情報の登録も行う

//TODO: 学生情報の更新処理を行う
  @Transactional
  public void updateStudent(StudentDetail studentDetail){
    repository.updateStudent(studentDetail.getStudent());
    //学生の登録情報を更新処理する
    for (StudentsCourses studentsCourses : studentDetail.getStudentsCourses()) {
    //  studentsCourses.setStudentId(studentDetail.getStudent().getId());
    //駄目な実装
      repository.updateStudentsCourses(studentsCourses);
    }

    }
  }


