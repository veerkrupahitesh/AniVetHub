package com.veeritsolution.android.anivethub.api;

import com.veeritsolution.android.anivethub.models.AppointmentModel;
import com.veeritsolution.android.anivethub.models.AppointmentRejectModel;
import com.veeritsolution.android.anivethub.models.CityModel;
import com.veeritsolution.android.anivethub.models.ClientCallFeedbackModel;
import com.veeritsolution.android.anivethub.models.ClientLoginModel;
import com.veeritsolution.android.anivethub.models.CountryModel;
import com.veeritsolution.android.anivethub.models.DegreeModel;
import com.veeritsolution.android.anivethub.models.DeviceTokenModel;
import com.veeritsolution.android.anivethub.models.GeneralSettingsModel;
import com.veeritsolution.android.anivethub.models.LocationModel;
import com.veeritsolution.android.anivethub.models.NotificationModel;
import com.veeritsolution.android.anivethub.models.PackageModel;
import com.veeritsolution.android.anivethub.models.PetDetailsModel;
import com.veeritsolution.android.anivethub.models.PetPicsModel;
import com.veeritsolution.android.anivethub.models.PetSymptomsModel;
import com.veeritsolution.android.anivethub.models.PetWeightModel;
import com.veeritsolution.android.anivethub.models.PractiseLoginModel;
import com.veeritsolution.android.anivethub.models.ReviewRatingModel;
import com.veeritsolution.android.anivethub.models.SearchVetModel;
import com.veeritsolution.android.anivethub.models.SessionModel;
import com.veeritsolution.android.anivethub.models.StateModel;
import com.veeritsolution.android.anivethub.models.SupportCenterModel;
import com.veeritsolution.android.anivethub.models.TimeSlotModel;
import com.veeritsolution.android.anivethub.models.TreatmentModel;
import com.veeritsolution.android.anivethub.models.UniversityModel;
import com.veeritsolution.android.anivethub.models.VetAccountInfoModel;
import com.veeritsolution.android.anivethub.models.VetEducation;
import com.veeritsolution.android.anivethub.models.VetExperience;
import com.veeritsolution.android.anivethub.models.VetLoginModel;
import com.veeritsolution.android.anivethub.models.VetPracticeUserModel;
import com.veeritsolution.android.anivethub.models.VetSessionRateModel;
import com.veeritsolution.android.anivethub.models.VetSpecialization;
import com.veeritsolution.android.anivethub.models.VetSubscriptionModel;
import com.veeritsolution.android.anivethub.models.VetTimeSlotModel;

/**
 * Created by ${hitesh} on 12/7/2016.
 */

public enum RequestCode {
    // Default(null),
    GetUser(null),
    ClientInsert(ClientLoginModel.class),
    VetInsert(VetLoginModel.class),
    PracInsert(PractiseLoginModel.class),
    ClientUpdate(ClientLoginModel.class),
    ClientPetUpdate(null),
    GetPetTypeInfo(null),
    GetPetTypeGroupInfo(null),
    GetPetBreedInfo(null),
    ClientPetInsert(null),
    NormalSearchVet(SearchVetModel[].class),
    FilterSearchVet(SearchVetModel[].class),
    SortSearchVet(SearchVetModel[].class),
    // getClientPetUpdate(PetDetailsModel[].class),
    GetClientPetInfoAll(PetDetailsModel[].class),
    GetClientPetInfo(PetDetailsModel.class),
    ClientPetDelete(PetDetailsModel[].class),
    GetCountryInfo(CountryModel[].class),
    GetStateInfo(StateModel[].class),
    GetCityInfo(CityModel[].class),
    // ClientProfilePicUpdate(null),
    // ClientBannerPicUpdate(null),
    // VetProfilePicUpdate(null),
    // VetBannerPicUpdate(null),
    VetUpdate(VetLoginModel.class),
    GetUniversityInfo(UniversityModel[].class),
    GetDegreeInfo(DegreeModel[].class),
    GetClientInfo(ClientLoginModel.class),
    GetVetInfo(VetLoginModel.class),
    GetVetInfoAll(null),
    GetPetSymptomsInfo(PetSymptomsModel[].class),
    ForgotPassword(null),
    GetVetEducationInfoAll(VetEducation[].class),
    GetVetEducationInfo(VetEducation.class),
    VetEducationInsert(null),
    VetEducationUpdate(null),
    VetEducationDelete(VetEducation[].class),
    GetVetExpertiseInfo(VetSpecialization.class),
    GetVetExpertiseInfoAll(VetSpecialization[].class),
    VetExpertiseInsert(null),
    VetExpertiseUpdate(null),
    VetExpertiseDelete(VetSpecialization[].class),
    VetVetExperienceInfoAll(VetExperience[].class),
    GetVetExperienceInfo(VetExperience.class),
    VetExperienceInsert(VetExperience.class),
    VetExperienceUpdate(null),
    VetExperienceDelete(VetExperience[].class),
    // GetVetClinicInfoAll(VetClinic[].class),
    //  GetVetClinicInfo(VetClinic.class),
    // VetClinicInsert(null),
    //  VetClinicUpdate(null),
    //  VetClinicDelete(VetClinic[].class),
    ChangePassword(null),
    VetStatusUpdate(null),
    // GetVetClinicTimingUpdate(null),
    // getVetClinicTimingInfoAll(VetClinicTimingInfo[].class),
    // GetVetClinicTimingInsert(null),
    SetClientLocation(LocationModel.class),
    SetVetLocation(LocationModel.class),
    GetGeneralSettings(GeneralSettingsModel.class),
    GetVetAccountInfo(VetAccountInfoModel.class),
    VetAccountInsert(null),
    VetSessionInsert(null),
    VetSessionUpdate(null),
    GetVetSubscriptionInfoAll(VetSubscriptionModel[].class),
    GetPackageSettings(PackageModel[].class),
    GetVetSessionInfoAll(SessionModel[].class),
    GetVetSessionInfoAllClient(SessionModel[].class),
    VetSubscriptionInsert(null),
    GetClientAppointmentInfo(AppointmentModel[].class),
    GetVetReviewRatings(ReviewRatingModel[].class),
    GetVetTimeSlotInfoAll(VetTimeSlotModel[].class),
    VetAppointmentInsert(null),
    GetVetAppointmentInfoAll(AppointmentModel[].class),
    GetVetAppointmentInfo(AppointmentModel[].class),
    GetTimeSubslotInfo(TimeSlotModel[].class),
    VetAppointmentApprove(null),
    VetAppointmentReject(null),
    GetRejectReasonInfo(AppointmentRejectModel[].class),
    VetTimeSlotDelete(null),
    GetTimeSlotInfo(TimeSlotModel[].class),
    VetTimeSlotInsert(null),
    SetVetTokenId(DeviceTokenModel[].class),
    SetClientTokenId(DeviceTokenModel[].class),
    GetPetWeightHistory(PetWeightModel[].class),
    GetCountry(CountryModel[].class),
    GetState(StateModel[].class),
    GetCity(CityModel[].class),
    VetSessionTicketInsert(null),
    SupportCenter(SupportCenterModel[].class),
    GetClientPetTreatmentInfoAll(TreatmentModel[].class),
    ClientPetTreatmentDelete(TreatmentModel[].class),
    ClientPetTreatmentInfo(TreatmentModel[].class),
    ClientPetTreatmentUpdate(null),
    ClientPetTreatmentInsert(TreatmentModel[].class),
    GetNotifications(NotificationModel[].class),
    VetSessionFeedbackInsert(null),
    GetFeedbackTypeInfo(ClientCallFeedbackModel[].class),
    VetRateDelete(null),
    VetRateInsert(null),
    VetRateUpdate(null),
    GetVetSessionRateInfoAll(VetSessionRateModel[].class),
    SearchVetPractise(SearchVetModel[].class),
    GetPracInfo(PractiseLoginModel.class),
    PracUpdate(PractiseLoginModel.class),
    GetVetPracAccountInfo(VetAccountInfoModel.class),
    VetPracAccountInsert(null),
    GetVetPractiseInfoAll(VetPracticeUserModel[].class),
    GetVetPractiseDelete(null),
    VetPractiseInsert(null),
    GetVetPractiseInfo(VetPracticeUserModel[].class),
    ClientProfilePhotoUpdate(ClientLoginModel.class),
    ClientBannerPhotoUpdate(ClientLoginModel.class),
    VetProfilePhotoUpdate(VetLoginModel.class),
    VetBannerPhotoUpdate(VetLoginModel.class),
    PractiseProfilePhotoUpload(PractiseLoginModel.class),
    PractiseBannerPhotoUpload(PractiseLoginModel.class),
    VetSessionUpdateVideoConsultSummary(null),
    GetPetPicsInfo(PetPicsModel[].class),
    PetPicDelete(null),
    VetSessionUpdateReview(null),
    VetAppointmentConform(null),
    VetAppointmentCancel(null),
    ClientAppointmentConfirm(null),
    ClientAppointmentCancel(null),
    ClientPetPicInsert(null);


    Class mLocalClass;

    RequestCode(Class mLocalClass) {

        this.mLocalClass = mLocalClass;
    }

    public Class getLocalClass() {
        return mLocalClass;
    }

    public void setLocalClass(Class mLocalClass) {
        this.mLocalClass = mLocalClass;
    }
}
