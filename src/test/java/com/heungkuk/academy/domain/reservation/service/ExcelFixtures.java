package com.heungkuk.academy.domain.reservation.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.heungkuk.academy.domain.reservation.entity.ClassroomReservation;
import com.heungkuk.academy.domain.reservation.entity.MealReservation;
import com.heungkuk.academy.domain.reservation.entity.Reservation;
import com.heungkuk.academy.domain.reservation.entity.RoomReservation;
import com.heungkuk.academy.domain.reservation.repository.ClassroomReservationRepository;
import com.heungkuk.academy.domain.reservation.repository.MealReservationRepository;
import com.heungkuk.academy.domain.reservation.repository.ReservationRepository;
import com.heungkuk.academy.domain.reservation.repository.RoomReservationRepository;
import com.heungkuk.academy.domain.setting.entity.AppSetting;
import com.heungkuk.academy.domain.setting.repository.AppSettingRepository;

/** 엑셀 생성 테스트용 가짜 데이터. 실제 고객/조직 정보는 전혀 사용하지 않는다. */
final class ExcelFixtures {

    static final long RESERVATION_ID = 1L;
    static final String ORGANIZATION = "테스트기업";

    final ReservationRepository reservationRepository = mock(ReservationRepository.class);
    final RoomReservationRepository roomReservationRepository = mock(RoomReservationRepository.class);
    final ClassroomReservationRepository classroomReservationRepository =
            mock(ClassroomReservationRepository.class);
    final MealReservationRepository mealReservationRepository = mock(MealReservationRepository.class);
    final AppSettingRepository appSettingRepository = mock(AppSettingRepository.class);

    ExcelFixtures() {
        LocalDate d1 = LocalDate.of(2030, 1, 10);
        LocalDate d2 = d1.plusDays(1);
        LocalDate d3 = d1.plusDays(2);

        Reservation res = Reservation.builder()
                .reservationCode("HK-20300110-001")
                .organization(ORGANIZATION)
                .purpose("테스트 연수")
                .people(30)
                .customer("테스트담당")
                .customerPhone("000-0000-0000")
                .customerPhone2("000-0000-0001")
                .customerEmail("customer@example.com")
                .startDate(d1)
                .endDate(d3)
                .colorCode("#4A90E2")
                .companyZipCode("00000")
                .companyAddress("테스트시 테스트구 테스트로 1")
                .businessNumber("000-00-00000")
                .ceoName("테스트대표")
                .siteManager("현장담당")
                .siteManagerPhone("000-0000-0002")
                .siteManagerEmail("site@example.com")
                .billingManager("정산담당")
                .billingManagerPhone("000-0000-0003")
                .billingManagerEmail("billing@example.com")
                .status("확정")
                .paymentMethod("계좌이체")
                .build();

        List<RoomReservation> rooms = List.of(
                room(res, "101", d1), room(res, "101", d2), room(res, "102", d1), room(res, "102", d2),
                room(res, "109", d1), room(res, "110", d1), room(res, "110", d2), room(res, "111", d2));
        List<ClassroomReservation> classrooms = List.of(
                classroom(res, "105", d1), classroom(res, "105", d2), classroom(res, "201", d1),
                classroom(res, "203", d2), classroom(res, "101", d1), classroom(res, "A", d3));
        List<MealReservation> meals = List.of(
                meal(res, d1, 0, 30, 30, false, false, false), meal(res, d2, 30, 30, 30, false, true, false),
                meal(res, d3, 30, 30, 0, true, false, false));

        List<AppSetting> settings = List.of(AppSetting.of("contact.phone", "000-0000-0000"),
                AppSetting.of("contact.fax", "000-0000-0001"), AppSetting.of("contact.email", "sample@example.com"));

        when(reservationRepository.findById(RESERVATION_ID)).thenReturn(Optional.of(res));
        when(roomReservationRepository.findByReservation(any())).thenReturn(rooms);
        when(classroomReservationRepository.findByReservation(any())).thenReturn(classrooms);
        when(mealReservationRepository.findByReservation(any())).thenReturn(meals);
        when(appSettingRepository.findAll()).thenReturn(settings);
    }

    private static RoomReservation room(Reservation r, String no, LocalDate d) {
        return RoomReservation.builder().reservation(r).roomNumber(no).reservedDate(d).build();
    }

    private static ClassroomReservation classroom(Reservation r, String name, LocalDate d) {
        return ClassroomReservation.builder().reservation(r).classroom(name).reservedDate(d).build();
    }

    private static MealReservation meal(Reservation r, LocalDate d, int b, int l, int dn, boolean sb,
            boolean sl, boolean sd) {
        return MealReservation.builder().reservation(r).mealDate(d).breakfast(b).lunch(l).dinner(dn)
                .specialBreakfast(sb).specialLunch(sl).specialDinner(sd).build();
    }
}
