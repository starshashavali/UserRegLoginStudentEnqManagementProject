package com.ibm.services.impl;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.ibm.binding.DashboardResponse;
import com.ibm.binding.EnquiryForm;
import com.ibm.binding.EnquirySearchCriteria;
import com.ibm.entities.StudentEnqEntity;
import com.ibm.entities.UserDtlsEntity;
import com.ibm.repository.CoursesRepository;
import com.ibm.repository.EnquiryStatusRepo;
import com.ibm.repository.StudentEnquiriesRepository;
import com.ibm.repository.UserRepository;
import com.ibm.services.EnquiryService;

@Service
public class EnquiryServiceImpl implements EnquiryService {

	@Autowired
	private StudentEnquiriesRepository studentRepo;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private CoursesRepository courseRepo;

	@Autowired
	private EnquiryStatusRepo statusRepo;

	@Override
	public Boolean saveEnquiry(EnquiryForm form, Integer userId) {

		Optional<UserDtlsEntity> findById = userRepo.findById(userId);
		UserDtlsEntity user = findById.get();

		if (form.getEId() != null) {
			
			StudentEnqEntity entity = studentRepo.findById(form.getEId()).get();
			entity.setClassMode(form.getClassMode());
			entity.setCourseName(form.getCourseName());
			entity.setEnquiryStatus(form.getEnquiryStatus());
			entity.setPhno(form.getPhno());
			entity.setStudentName(form.getStudentName());
			entity.setUser(user);
			
			entity.setUser(user);

			studentRepo.save(entity);

			return true;
		}

		StudentEnqEntity entity = new StudentEnqEntity();

		BeanUtils.copyProperties(form, entity);

		entity.setUser(user);

		studentRepo.save(entity);

		return true;
	}

	@Override
	public List<StudentEnqEntity> getEnquiries(Integer userId, EnquirySearchCriteria search) {

		StudentEnqEntity entity = new StudentEnqEntity();

		Optional<UserDtlsEntity> findById = userRepo.findById(userId);
		UserDtlsEntity user = findById.get();

		BeanUtils.copyProperties(search, entity);

		entity.setUser(user);

		Example<StudentEnqEntity> example = Example.of(entity);
		List<StudentEnqEntity> list = studentRepo.findAll(example);

		return list;
	}

	@Override
	public DashboardResponse getDashboardData(Integer userId) {

		Optional<UserDtlsEntity> findById = userRepo.findById(userId);
		UserDtlsEntity user = findById.get();

		List<StudentEnqEntity> enquiries = user.getLstOfEnquiries();
		long enrolledCount = enquiries.stream().filter(e -> e.getEnquiryStatus().equalsIgnoreCase("enrolled")).count();

		long lostCnt = enquiries.stream().filter(e -> e.getEnquiryStatus().equalsIgnoreCase("lost")).count();

		long totalCount = enquiries.stream().count();

		DashboardResponse data = new DashboardResponse();

		data.setEnrolledEnquiries((int) enrolledCount);
		data.setLostEnquiries((int) lostCnt);
		data.setNoOfEnquiries((int) totalCount);

		return data;
	}/*
	public DashboardResponse getDashboardData(Integer userId) {
    UserDtlsEntity user = userRepo.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

    List<StudentEnqEntity> enquiries = user.getLstOfEnquiries();

    long enrolledCount = enquiries.stream().filter(e -> e.getEnquiryStatus().equalsIgnoreCase("enrolled")).count();
    long lostCount = enquiries.stream().filter(e -> e.getEnquiryStatus().equalsIgnoreCase("lost")).count();
    long totalCount = enquiries.size();

    return new DashboardResponse((int) enrolledCount, (int) lostCount, (int) totalCount);
}
*/

	@Override
	public List<String> getCourseNames() {
		List<String> courses = courseRepo.getCourses();
		return courses;
	}

	@Override
	public List<String> getEnquiryStatus() {
		List<String> statusNames = statusRepo.getStatusNames();
		return statusNames;
	}/*
	public List<String> getEnquiryStatus() {
    return statusRepo.getStatusNames().stream().collect(Collectors.toList());
}*/


	@Override
	public StudentEnqEntity getStudentEnq(Integer eId) {
		Optional<StudentEnqEntity> findById = studentRepo.findById(eId);
		StudentEnqEntity entityObj = findById.get();
		return entityObj;
	}

}
