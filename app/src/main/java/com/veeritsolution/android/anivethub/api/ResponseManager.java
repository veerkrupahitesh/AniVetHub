package com.veeritsolution.android.anivethub.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.veeritsolution.android.anivethub.models.AppointmentRejectModel;
import com.veeritsolution.android.anivethub.models.CityModel;
import com.veeritsolution.android.anivethub.models.ClientCallFeedbackModel;
import com.veeritsolution.android.anivethub.models.ClientLoginModel;
import com.veeritsolution.android.anivethub.models.CountryModel;
import com.veeritsolution.android.anivethub.models.DegreeModel;
import com.veeritsolution.android.anivethub.models.DeviceTokenModel;
import com.veeritsolution.android.anivethub.models.NotificationModel;
import com.veeritsolution.android.anivethub.models.PetBreedModel;
import com.veeritsolution.android.anivethub.models.PetDetailsModel;
import com.veeritsolution.android.anivethub.models.PetPicsModel;
import com.veeritsolution.android.anivethub.models.PetSymptomsModel;
import com.veeritsolution.android.anivethub.models.PetTypeGroupModel;
import com.veeritsolution.android.anivethub.models.PetTypeModel;
import com.veeritsolution.android.anivethub.models.PetWeightModel;
import com.veeritsolution.android.anivethub.models.PracticeModel;
import com.veeritsolution.android.anivethub.models.PractiseLoginModel;
import com.veeritsolution.android.anivethub.models.SearchVetModel;
import com.veeritsolution.android.anivethub.models.StateModel;
import com.veeritsolution.android.anivethub.models.SupportCenterModel;
import com.veeritsolution.android.anivethub.models.TimeSlotModel;
import com.veeritsolution.android.anivethub.models.TreatmentModel;
import com.veeritsolution.android.anivethub.models.UniversityModel;
import com.veeritsolution.android.anivethub.models.VetEducation;
import com.veeritsolution.android.anivethub.models.VetExperience;
import com.veeritsolution.android.anivethub.models.VetLoginModel;
import com.veeritsolution.android.anivethub.models.VetPracticeUserModel;
import com.veeritsolution.android.anivethub.models.VetSessionRateModel;
import com.veeritsolution.android.anivethub.models.VetSpecialization;
import com.veeritsolution.android.anivethub.models.VetTimeSlotModel;
import com.veeritsolution.android.anivethub.utility.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by ${hitesh} on 12/8/2016.
 */

public class ResponseManager {

    public static <T> Object parseResponse(String mResponse, RequestCode mRequestCode, Gson mGson) {

        Object object = null;

        try {
            JSONArray jsonArray = new JSONArray(mResponse);
            JSONObject jsonObject = null;

            switch (mRequestCode) {

                case GetUser:

                    jsonObject = jsonArray.getJSONObject(0);
                    int dataId = jsonObject.getInt("DataId");

                    if (dataId == Constants.VET_DATA_ID) {

                        int clinicDataId = jsonObject.getInt("IsVetPractiseUser");

                        if (clinicDataId == Constants.CLINIC_DATA_ID) {
                            PractiseLoginModel.savePractiseCredentials(jsonObject.toString());
                            //PrefHelper.getInstance().setInt(PrefHelper.LOGIN_USER, Constants.CLINIC_LOGIN);
                            object = mGson.fromJson(jsonObject.toString(), PractiseLoginModel.class);
                            // PrefHelper.getInstance().setBoolean(PrefHelper.IS_LOGIN, true);
                        } else {
                            VetLoginModel.saveVetCredentials(jsonObject.toString());
                            // PrefHelper.getInstance().setInt(PrefHelper.LOGIN_USER, Constants.VET_LOGIN);
                            object = mGson.fromJson(jsonObject.toString(), VetLoginModel.class);
                            // PrefHelper.getInstance().setBoolean(PrefHelper.IS_LOGIN, true);
                        }

                    } else {

                        ClientLoginModel.saveClientCredentials(jsonObject.toString());
                        // PrefHelper.getInstance().setInt(PrefHelper.LOGIN_USER, Constants.CLIENT_LOGIN);
                        //  PrefHelper.getInstance().setBoolean(PrefHelper.IS_LOGIN, true);
                        object = mGson.fromJson(String.valueOf(jsonObject), ClientLoginModel.class);

                    }
                    break;

                case ClientInsert:

                    jsonObject = jsonArray.getJSONObject(0);

                    if (jsonObject.has("IsDataExists")) {
                        object = mGson.fromJson(jsonObject.toString(), mRequestCode.getLocalClass());
                    } else {
                        ClientLoginModel.saveClientCredentials(jsonObject.toString());
                        object = mGson.fromJson(jsonObject.toString(), mRequestCode.getLocalClass());
                    }
                    break;

                case VetInsert:

                    jsonObject = jsonArray.getJSONObject(0);

                    if (jsonObject.has("IsDataExists")) {
                        object = mGson.fromJson(jsonObject.toString(), mRequestCode.getLocalClass());
                    } else {
                        VetLoginModel.saveVetCredentials(jsonObject.toString());
                        object = mGson.fromJson(jsonObject.toString(), mRequestCode.getLocalClass());
                    }
                    break;

                case PracInsert:

                    jsonObject = jsonArray.getJSONObject(0);

                    if (jsonObject.has("IsDataExists")) {
                        object = mGson.fromJson(jsonObject.toString(), mRequestCode.getLocalClass());
                    } else {
                        PractiseLoginModel.savePractiseCredentials(jsonObject.toString());
                        object = mGson.fromJson(jsonObject.toString(), mRequestCode.getLocalClass());
                    }
                    break;

                case GetClientInfo:

                    jsonObject = jsonArray.getJSONObject(0);
                    object = mGson.fromJson(jsonObject.toString(), mRequestCode.getLocalClass());
                    break;

                case ClientUpdate:

                    jsonObject = jsonArray.getJSONObject(0);
                    ClientLoginModel.saveClientCredentials(jsonObject.toString());
                    object = mGson.fromJson(jsonObject.toString(), mRequestCode.getLocalClass());
                    break;

                case VetUpdate:

                    jsonObject = jsonArray.getJSONObject(0);
                    VetLoginModel.saveVetCredentials(jsonObject.toString());
                    object = mGson.fromJson(jsonObject.toString(), mRequestCode.getLocalClass());
                    break;

                case GetPetTypeInfo:

                    object = mGson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<PetTypeModel>>() {
                    }.getType());
                    break;

                case GetPetBreedInfo:

                    object = mGson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<PetBreedModel>>() {
                    }.getType());
                    break;

                case ClientPetInsert:

                    object = jsonArray;
                    break;

                case ClientPetUpdate:

                    object = jsonArray;
                    break;

                case NormalSearchVet:

                    object = mGson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<SearchVetModel>>() {
                    }.getType());
                    break;

                case FilterSearchVet:

                    object = mGson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<SearchVetModel>>() {
                    }.getType());
                    break;

                case SortSearchVet:

                    object = mGson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<SearchVetModel>>() {
                    }.getType());
                    break;

                case GetClientPetInfoAll:

                    object = mGson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<PetDetailsModel>>() {
                    }.getType());

                    // object = mGson.fromJson(jsonArray.toString(), mRequestCode.getLocalClass());
                    // object = Arrays.asList((T[]) object);
                    break;

                case ClientPetDelete:

                    jsonObject = jsonArray.getJSONObject(0);
                    if (jsonObject.has("DataId")) {
                        object = null;
                    } else {
                        object = mGson.fromJson(jsonArray.toString(), mRequestCode.getLocalClass());
                        object = Arrays.asList((T[]) object);
                    }

                    break;

                case GetCountryInfo:

                    object = jsonArray;
                    break;

                case GetStateInfo:
                    object = jsonArray;
                    break;

                case GetCityInfo:
                    object = jsonArray;
                    break;

                case GetUniversityInfo:

                    object = mGson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<UniversityModel>>() {
                    }.getType());
                    break;

                case GetDegreeInfo:

                    object = mGson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<DegreeModel>>() {
                    }.getType());
                    break;

                case GetVetInfo:

                    jsonObject = jsonArray.getJSONObject(0);
                    object = mGson.fromJson(jsonObject.toString(), mRequestCode.getLocalClass());
                    break;

                case GetVetInfoAll:

                    object = jsonArray;
                    break;

                case GetPetSymptomsInfo:

                    object = mGson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<PetSymptomsModel>>() {
                    }.getType());
                    break;

                case ForgotPassword:

                    object = jsonArray;
                    break;

                case GetVetEducationInfoAll:

                    object = mGson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<VetEducation>>() {
                    }.getType());
                    break;

                case GetVetEducationInfo:
                    jsonObject = jsonArray.getJSONObject(0);
                    object = mGson.fromJson(jsonObject.toString(), mRequestCode.getLocalClass());
                    break;

                case VetEducationInsert:

                    object = jsonArray;
                    break;

                case VetEducationUpdate:

                    object = jsonArray;
                    break;

                case VetEducationDelete:

                    object = mGson.fromJson(jsonArray.toString(), mRequestCode.getLocalClass());
                    object = Arrays.asList((T[]) object);
                    break;

                case GetVetExpertiseInfoAll:

                    object = mGson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<VetSpecialization>>() {
                    }.getType());
                    break;

                case GetVetExpertiseInfo:

                    jsonObject = jsonArray.getJSONObject(0);
                    object = mGson.fromJson(jsonObject.toString(), mRequestCode.getLocalClass());
                    break;

                case VetExpertiseInsert:

                    object = jsonArray;
                    break;

                case VetExpertiseUpdate:

                    object = jsonArray;
                    break;

                case VetExpertiseDelete:

                    object = mGson.fromJson(jsonArray.toString(), mRequestCode.getLocalClass());
                    object = Arrays.asList((T[]) object);
                    break;

                case VetVetExperienceInfoAll:

                    object = mGson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<VetExperience>>() {
                    }.getType());
                    break;

                case GetVetExperienceInfo:

                    jsonObject = jsonArray.getJSONObject(0);
                    object = mGson.fromJson(jsonObject.toString(), mRequestCode.getLocalClass());
                    break;

                case VetExperienceInsert:

                    jsonObject = jsonArray.getJSONObject(0);
                    object = mGson.fromJson(jsonObject.toString(), mRequestCode.getLocalClass());
                    break;

                case VetExperienceUpdate:

                    object = jsonArray;
                    break;

                case VetExperienceDelete:

                    object = jsonArray;
                    break;

                case GetClientPetInfo:

                    jsonObject = jsonArray.getJSONObject(0);
                    object = mGson.fromJson(jsonObject.toString(), mRequestCode.getLocalClass());
                    break;

                case ChangePassword:

                    object = jsonArray;
                    break;

                case VetStatusUpdate:

                    object = jsonArray;
                    break;

                case SetVetLocation:

                    jsonObject = jsonArray.getJSONObject(0);
                    object = mGson.fromJson(jsonObject.toString(), mRequestCode.getLocalClass());
                    break;

                case SetClientLocation:

                    jsonObject = jsonArray.getJSONObject(0);
                    object = mGson.fromJson(jsonObject.toString(), mRequestCode.getLocalClass());
                    break;

                case GetGeneralSettings:

                    jsonObject = jsonArray.getJSONObject(0);
                    object = mGson.fromJson(jsonObject.toString(), mRequestCode.getLocalClass());
                    break;

                case GetVetAccountInfo:

                    jsonObject = jsonArray.getJSONObject(0);
                    object = mGson.fromJson(jsonObject.toString(), mRequestCode.getLocalClass());
                    break;

                case GetVetPracAccountInfo:
                    jsonObject = jsonArray.getJSONObject(0);
                    object = mGson.fromJson(jsonObject.toString(), mRequestCode.getLocalClass());
                    break;

                case VetAccountInsert:

                    object = jsonArray;
                    break;

                case VetPracAccountInsert:
                    object = jsonArray;
                    break;

                case VetSessionInsert:

                    object = jsonArray;
                    break;

                case VetSessionUpdate:

                    object = jsonArray;
                    break;

                case GetVetSubscriptionInfoAll:

                    object = mGson.fromJson(jsonArray.toString(), mRequestCode.getLocalClass());
                    object = Arrays.asList((T[]) object);
                    break;

                case GetPackageSettings:

                    object = mGson.fromJson(jsonArray.toString(), mRequestCode.getLocalClass());
                    object = Arrays.asList((T[]) object);
                    break;

                case GetVetSessionInfoAll:

                    object = mGson.fromJson(jsonArray.toString(), mRequestCode.getLocalClass());
                    object = Arrays.asList((T[]) object);
                    break;

                case GetVetSessionInfoAllClient:

                    object = mGson.fromJson(jsonArray.toString(), mRequestCode.getLocalClass());
                    object = Arrays.asList((T[]) object);
                    break;

                case VetSubscriptionInsert:

                    object = jsonArray;
                    break;

                case GetClientAppointmentInfo:

                    object = mGson.fromJson(jsonArray.toString(), mRequestCode.getLocalClass());
                    object = Arrays.asList((T[]) object);
                    break;

                case GetVetReviewRatings:

                    object = mGson.fromJson(jsonArray.toString(), mRequestCode.getLocalClass());
                    object = Arrays.asList((T[]) object);
                    break;

                case GetVetTimeSlotInfoAll:

                    object = mGson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<VetTimeSlotModel>>() {
                    }.getType());
                    break;

                case VetAppointmentInsert:

                    object = jsonArray;
                    break;

                case GetVetAppointmentInfoAll:

                    object = mGson.fromJson(jsonArray.toString(), mRequestCode.getLocalClass());
                    object = Arrays.asList((T[]) object);
                    break;

                case GetVetAppointmentInfo:

                    object = mGson.fromJson(jsonArray.toString(), mRequestCode.getLocalClass());
                    object = Arrays.asList((T[]) object);
                    break;

                case GetTimeSubslotInfo:

                    object = mGson.fromJson(jsonArray.toString(), mRequestCode.getLocalClass());
                    object = Arrays.asList((T[]) object);
                    break;

                case VetAppointmentApprove:

                    object = jsonArray;
                    break;

                case VetAppointmentReject:

                    object = jsonArray;
                    break;

                case GetRejectReasonInfo:

                    object = mGson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<AppointmentRejectModel>>() {
                    }.getType());
                    break;

                case VetTimeSlotDelete:

                    object = jsonArray;
                    break;

                case GetTimeSlotInfo:

                    object = mGson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<TimeSlotModel>>() {
                    }.getType());
                    break;

                case VetTimeSlotInsert:

                    object = jsonArray;
                    break;

                case SetClientTokenId:

                    object = mGson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<DeviceTokenModel>>() {
                    }.getType());
                    break;

                case SetVetTokenId:

                    object = mGson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<DeviceTokenModel>>() {
                    }.getType());
                    break;

                case GetPetWeightHistory:

                    object = mGson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<PetWeightModel>>() {
                    }.getType());
                    break;

                case GetCountry:

                    object = mGson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<CountryModel>>() {
                    }.getType());
                    break;

                case GetState:

                    object = mGson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<StateModel>>() {
                    }.getType());
                    break;

                case GetCity:

                    object = mGson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<CityModel>>() {
                    }.getType());
                    break;

                case VetSessionTicketInsert:

                    object = jsonArray;
                    break;

                case SupportCenter:

                    object = mGson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<SupportCenterModel>>() {
                    }.getType());
                    break;

                case GetClientPetTreatmentInfoAll:

                    object = mGson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<TreatmentModel>>() {
                    }.getType());
                    break;

                case ClientPetTreatmentInfo:

                    object = mGson.fromJson(jsonArray.toString(), mRequestCode.getLocalClass());
                    object = Arrays.asList((T[]) object);
                    break;

                case ClientPetTreatmentUpdate:

                    object = jsonArray;
                    break;

                case ClientPetTreatmentDelete:

                    object = jsonArray;
                    break;

                case ClientPetTreatmentInsert:

                    object = mGson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<TreatmentModel>>() {
                    }.getType());
                    break;

                case GetNotifications:

                    object = mGson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<NotificationModel>>() {
                    }.getType());
                    break;

                case GetFeedbackTypeInfo:

                    object = mGson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<ClientCallFeedbackModel>>() {
                    }.getType());
                    break;

                case GetVetSessionRateInfoAll:

                    object = mGson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<VetSessionRateModel>>() {
                    }.getType());
                    break;

                case VetRateDelete:

                    object = mGson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<VetSessionRateModel>>() {
                    }.getType());
                    break;

                case SearchVetPractise:

                    object = mGson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<SearchVetModel>>() {
                    }.getType());
                    break;

                case GetPracInfo:

                    jsonObject = jsonArray.getJSONObject(0);
                    object = mGson.fromJson(jsonObject.toString(), mRequestCode.getLocalClass());
                    break;

                case PracUpdate:

                    jsonObject = jsonArray.getJSONObject(0);
                    PractiseLoginModel.savePractiseCredentials(jsonObject.toString());
                    object = mGson.fromJson(jsonObject.toString(), mRequestCode.getLocalClass());
                    break;

                case GetVetPractiseInfoAll:

                    object = mGson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<VetPracticeUserModel>>() {
                    }.getType());
                    break;

                case GetVetPractiseDelete:

                    object = jsonArray;
                    break;

                case GetVetPractiseInfo:

                    object = mGson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<VetPracticeUserModel>>() {
                    }.getType());
                    break;

                case ClientProfilePhotoUpdate:

                    jsonObject = jsonArray.getJSONObject(0);
                    object = jsonObject;/*mGson.fromJson(jsonObject.toString(), mRequestCode.getLocalClass());*/
                    break;

                case ClientBannerPhotoUpdate:

                    jsonObject = jsonArray.getJSONObject(0);
                    object = jsonObject;
                    break;

                case VetProfilePhotoUpdate:

                    jsonObject = jsonArray.getJSONObject(0);
                    object = jsonObject;/* mGson.fromJson(jsonObject.toString(), mRequestCode.getLocalClass());*/
                    break;

                case VetBannerPhotoUpdate:

                    jsonObject = jsonArray.getJSONObject(0);
                    object = jsonObject; /*mGson.fromJson(jsonObject.toString(), mRequestCode.getLocalClass());*/
                    break;

                case PractiseBannerPhotoUpload:
                    jsonObject = jsonArray.getJSONObject(0);
                    object = jsonObject;
                    break;

                case PractiseProfilePhotoUpload:

                    jsonObject = jsonArray.getJSONObject(0);
                    object = jsonObject;
                    break;

                case GetPetPicsInfo:

                    object = mGson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<PetPicsModel>>() {
                    }.getType());
                    break;

                case PetPicDelete:

                    object = jsonArray;
                    break;

                case VetSessionUpdateReview:

                    object = jsonArray;
                    break;

                case VetAppointmentConform:

                    object = jsonArray;
                    break;

                case VetAppointmentCancel:

                    object = jsonArray;
                    break;

                case ClientAppointmentConfirm:

                    object = jsonArray;
                    break;

                case ClientAppointmentCancel:

                    object = jsonArray;
                    break;

                case VetPractiseInsert:

                    object = jsonArray;
                    break;

                case VetRateInsert:

                    object = jsonArray;
                    break;

                case VetRateUpdate:

                    object = jsonArray;
                    break;

                case VetSessionUpdateVideoConsultSummary:

                    object = jsonArray;
                    break;

                case GetPetTypeGroupInfo:
                    object = mGson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<PetTypeGroupModel>>() {
                    }.getType());
                    break;

                case VetSessionFeedbackInsert:

                    object = jsonArray;
                    break;

                case ClientPetPicInsert:
                    object = jsonArray;
                    break;

                case GetPracticeVet:
                    object = mGson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<PracticeModel>>() {
                    }.getType());
                    break;

                case AcceptVet:
                    object = jsonArray.getJSONObject(0);
                    break;

                case RejectVet:
                    object = jsonArray.getJSONObject(0);
                    break;

              /*  case ClientProfilePicUpdate:

                    object = mResponse;
                    break;

                case ClientBannerPicUpdate:

                    object = jsonArray;
                    break;

                case VetProfilePicUpdate:

                    object = jsonArray;
                    break;

                case VetBannerPicUpdate:

                    object = jsonArray;
                    break;*/

               /* case GetVetClinicInfoAll:

                    object = mGson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<VetClinic>>() {
                    }.getType());
                    break;

                case GetVetClinicInfo:

                    jsonObject = jsonArray.getJSONObject(0);
                    object = mGson.fromJson(jsonObject.toString(), mRequestCode.getLocalClass());
                    break;

                case VetClinicInsert:

                    object = jsonArray;
                    break;

                case VetClinicUpdate:

                    object = jsonArray;
                    break;

                case VetClinicDelete:

                    object = mGson.fromJson(jsonArray.toString(), mRequestCode.getLocalClass());
                    object = Arrays.asList((T[]) object);
                    break;

                case getVetClinicTimingInfoAll:

                    object = mGson.fromJson(jsonArray.toString(), mRequestCode.getLocalClass());
                    object = Arrays.asList((T[]) object);
                    break;

                case GetVetClinicTimingInsert:

                    object = jsonArray;
                    break;

                case GetVetClinicTimingUpdate:

                    object = jsonArray;
                    break;*/

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object;
    }
}
