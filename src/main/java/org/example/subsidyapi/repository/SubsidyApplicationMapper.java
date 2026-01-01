package org.example.subsidyapi.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.subsidyapi.controller.TotalAmountByStatusResponse;
import org.example.subsidyapi.subsidy.ApplicationStatus;
import org.example.subsidyapi.subsidy.SubsidyApplication;

@Mapper
public interface SubsidyApplicationMapper {

  List<SubsidyApplication> findAll();

  SubsidyApplication findById(long id);

  List<SubsidyApplication> findByStatus(ApplicationStatus status);

  BigDecimal sumAmount();

  BigDecimal sumAmountByStatus(ApplicationStatus status);

  List<TotalAmountByStatusResponse> sumAmountGroupedByStatus();

  int insert(SubsidyApplicationCreateCommand command);

  int update(@Param("id") long id,
      @Param("applicantName") String applicantName,
      @Param("applicationDate") LocalDate applicationDate,
      @Param("amount") BigDecimal amount,
      @Param("status") ApplicationStatus status);

  int delete(long id);
}