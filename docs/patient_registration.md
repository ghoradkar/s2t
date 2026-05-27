# Patient Registration Module — Complete Reference

> **Purpose:** One-stop reference for both Regular Camp and Door-to-Door (D2D) patient
> registration. Read this before touching any registration file. Updated: 2026-05-27.

---

## Table of Contents

1. [Module Overview](#1-module-overview)
2. [File Structure](#2-file-structure)
3. [Native Android Reference](#3-native-android-reference)
4. [Entry Points — How User Reaches Registration](#4-entry-points--how-user-reaches-registration)
5. [Regular Registration — Top to Bottom](#5-regular-registration--top-to-bottom)
6. [D2D Registration — Top to Bottom](#6-d2d-registration--top-to-bottom)
7. [ABHA Integration (D2D Only)](#7-abha-integration-d2d-only)
8. [Shared Sub-Screens](#8-shared-sub-screens)
9. [APIs — Full Reference](#9-apis--full-reference)
10. [Models Reference](#10-models-reference)
11. [Business Rules & Validations](#11-business-rules--validations)
12. [Known Gotchas & Pitfalls](#12-known-gotchas--pitfalls)

---

## 1. Module Overview

There are **two separate registration flows**:

| Feature | Regular Camp | Door-to-Door (D2D) |
|---|---|---|
| Camp type | `navCampType = '1'` | `navCampType = '3'` |
| Navigation type | `navType = '6'` | `navType = '6'` (default), `'5'` = recollection, `'7'` = registered by call |
| Dependent support | ❌ No | ✅ Yes |
| ABHA creation/linking | ❌ No | ✅ Yes |
| Face detection | Camera (no liveness) | FaceDetectionScreen (liveness) |
| Location capture | ❌ No | ✅ Yes (GPS, every 5 s) |
| Gram Panchayat | ❌ No | ✅ Yes (Rural/Urban toggle) |
| Ration card photo | ❌ No | ✅ Yes (after signature screen) |
| HIV letter photo | ❌ No | ✅ Yes |
| Mobile OTP verification | ❌ No | ✅ Yes (required for without_abha) |
| 365-day re-reg block | ✅ Yes | ✅ Yes |
| Submit endpoint | `BeneficiaryRe_RegistrationNew.ashx` | `handler/BeneficiaryRegistrationNew.ashx` |

---

## 2. File Structure

```
lib/Screens/patient_registration/
│
├── screen/
│   ├── regular_patient_registration_screen.dart   ← Regular form UI
│   ├── d2d_patient_registration_screen.dart        ← D2D form UI
│   ├── select_camp_screen.dart                     ← Camp picker (Regular)
│   ├── d2d_select_camp_screen.dart                 ← Camp picker (D2D)
│   ├── patient_finger_signature_screen.dart        ← Finger + signature (shared)
│   ├── patient_signature_screen.dart               ← Signature pad sub-screen
│   ├── ration_card_photo_screen.dart               ← Ration card upload (D2D)
│   ├── registered_patient_list_screen.dart         ← List of registered patients
│   ├── view_queue_patient_screen.dart              ← Patient queue viewer
│   ├── abha_creation_screen.dart                   ← ABHA creation sub-flow
│   ├── abha_address_creation_screen.dart           ← ABHA address selection
│   ├── abha_demographic_creation_screen.dart       ← Demographic ABHA path
│   └── abha_success_screen.dart                    ← Show ABHA card after create
│
├── controller/
│   ├── regular_patient_registration_controller.dart  ← Regular logic
│   ├── d2d_patient_registration_controller.dart      ← D2D logic (3087 lines)
│   ├── select_camp_controller.dart                   ← Regular camp picker
│   ├── d2d_select_camp_controller.dart               ← D2D camp picker
│   ├── patient_finger_signature_controller.dart      ← Finger+sign logic
│   ├── ration_card_photo_controller.dart             ← Ration card upload logic
│   ├── abha_creation_controller.dart                 ← ABHA Aadhaar OTP flow
│   ├── abha_address_creation_controller.dart         ← ABHA address creation
│   ├── abha_demographic_creation_controller.dart     ← ABHA demographic path
│   └── abha_success_controller.dart                  ← ABHA success display
│
├── repository/
│   ├── regular_patient_registration_repository.dart  ← Regular API calls
│   └── d2d_patient_registration_repository.dart      ← D2D API calls
│
└── model/
    ├── worker_info_response.dart         ← GetWorkerInfoWithMaritalStatus
    ├── beneficiary_details_response.dart ← BOCW + re-registration check
    ├── dependent_list_response.dart      ← GetDependentDetailsFromBoardData
    ├── district_list_response.dart       ← BindDistrict
    ├── document_type_response.dart       ← GetDocumentTypeList
    ├── gp_item.dart                      ← Gram Panchayat list item
    ├── regular_registration_response.dart← Save regular response
    ├── d2d_registration_response.dart    ← Save D2D response (has regdId)
    ├── select_camp_response.dart         ← Regular camp list
    ├── d2d_camp_response.dart            ← D2D camp list
    ├── attendance_status_response.dart   ← Attendance check
    ├── patient_details_on_reg_no_response.dart ← GetWorkerInfroFromWorkerRegid
    └── get_queue_response_model.dart     ← Patient queue data
```

---

## 3. Native Android Reference

| Native File | Purpose | Flutter Equivalent |
|---|---|---|
| `PatientRegistration_Activity.java` | Regular registration form | `regular_patient_registration_screen.dart` |
| `D2DPatientRegistration_Activity.java` | D2D registration form (main file ~15,000 lines) | `d2d_patient_registration_screen.dart` |
| `SelectCampActivity.java` | Camp selection for regular | `select_camp_screen.dart` |
| `D2DSelectCampActivity.java` | Camp selection for D2D | `d2d_select_camp_screen.dart` |
| `PatientFingerAndSignatureActivity.java` | Finger + signature capture | `patient_finger_signature_screen.dart` |
| `RationCardPhotoActivity.java` | Ration card upload | `ration_card_photo_screen.dart` |
| `AbhaCreationActivity.java` | ABHA Aadhaar OTP creation | `abha_creation_screen.dart` |
| `AbhaAddressCreationActivity.java` | ABHA address selection | `abha_address_creation_screen.dart` |
| `AbhaDemographicCreationActivity.java` | ABHA demographic path | `abha_demographic_creation_screen.dart` |
| `AbhaSuccessActivity.java` | Show ABHA card | `abha_success_screen.dart` |
| `Utilities.java` | Verhoeff Aadhaar validation, date utils | Inlined in `d2d_patient_registration_controller.dart` |
| `VerhoeffAlgorithm.java` | Aadhaar Verhoeff checksum tables | `_verhoeffValidate()` in controller |
| `FaceDetectionActivity.java` | Liveness face check | `FaceDetectionScreen.dart` |

---

## 4. Entry Points — How User Reaches Registration

### Regular Camp
```
Dashboard → Select Camp (SelectCampScreen)
             ↓ [User picks camp + date]
           RegularPatientRegistrationScreen
             ↓ [On submit success]
           PatientFingerAndSignatureScreen
             ↓ [On finger+sign success]
           Back to registration (form cleared)
```

### D2D (Door-to-Door)
```
Dashboard → D2D Select Camp (D2DSelectCampScreen)
             ↓ [User picks district → camp → date]
           D2DPatientRegistrationScreen
             ↓ [Optionally: ABHA creation/linking sub-flow]
           AbhaCreationScreen / AbhaSuccessScreen
             ↓ [Back to D2D form, ABHA pre-filled]
           D2DPatientRegistrationScreen [submit]
             ↓ [On submit success]
           PatientFingerAndSignatureScreen
             ↓ [On finger success, if dependent]
           RationCardPhotoScreen
             ↓ [On upload success]
           Back to D2D form (form cleared)
```

**Navigation parameters passed to D2DPatientRegistrationScreen:**
```dart
navCampId       // String — selected camp ID
navCampLocation // String — camp location/name
navSiteId       // String — site detail ID
navDistLgd      // String — district LGD code
navType         // '6'=normal, '5'=recollection, '7'=registered by call
navCampType     // '1'=Regular Camp, '3'=D2D Camp
navBeneficiaryNo // String — pre-fill reg no (from queue)
```

---

## 5. Regular Registration — Top to Bottom

### 5.1 Controller: `RegularPatientRegistrationController`

**Initialisation** (`onInit`):
- Reads `empCode` and `subOrgId` from `DataProvider().getParsedUserData()`

**Text Controllers (form fields):**

| Controller | Field Label | Validation |
|---|---|---|
| `tecWorkerRegNo` | Beneficiary Reg. No | Exactly 12 digits; triggers API fetch on 12th digit |
| `tecFullName` | English Name | Only `[A-Za-z ]`, non-empty |
| `tecMobileNo` | Mobile No | 10 digits |
| `tecAadhaarNo` | Aadhaar No | 12 digits |
| `tecDob` | Date of Birth | Non-empty; age 18–60 |
| `tecAge` | Age | Auto-calculated from DOB; 18–60 |
| `tecLocalAddr` | Local Address | Non-empty |
| `tecPermAddr` | Permanent Address | (Optional at submit) |
| `tecPincode` | Pin Code | 6 digits |
| `tecCardRegDate` | Card Registration Date | Required |
| `tecCardExpiry` | Card Expiry Date | Required; triggers renewal section if expired |
| `tecRenewalDate` | Renewal Date | Required only when `showRenewal = true` |

**Observable state:**

| Field | Type | Meaning |
|---|---|---|
| `selectedTitle` | `RxString` | 'Mr', 'Mrs', 'Ms', 'Dr' etc. |
| `selectedGender` | `RxString` | 'M' or 'F' |
| `isHCRenewal` | `RxBool` | Health card expired → renewal required |
| `showRenewal` | `RxBool` | Show renewal date + photo fields |
| `patientPhotoPath` | `RxString` | Local file path after camera capture |
| `healthCardPhotoPath` | `RxString` | Local file path |
| `renewalFormPath` | `RxString` | Local file path (only when renewal) |
| `patientPhotoUrl` | `RxString` | Server URL (pre-fill from re-registration) |
| `healthCardPhotoUrl` | `RxString` | Server URL |
| `renewalPhotoUrl` | `RxString` | Server URL |
| `isReregistration` | `bool` | True when API returns existing registration date |
| `isLoadingBeneficiary` | `RxBool` | Spinner while fetching BOCW data |
| `isSubmitting` | `RxBool` | Spinner during save |

### 5.2 Beneficiary Lookup (`onWorkerRegNoChanged` → `_fetchBeneficiary`)

Triggered when reg no reaches **12 digits**. Two parallel API calls:

1. **BOCW API** (`GET mahabocw-base/bocw-registration/{regNo}`) — external board data
2. **Internal check** (`POST kGetWorkerInfroReRegistration`) — checks if worker registered before

**Priority:** Internal result wins (`internalResult?.output ?? bocwResult?.output`)

**365-day block:** If internal registration date exists and `DateTime.now().difference < 365 days`:
- Show alert dialog
- Clear reg no field
- Return without filling form

**Fields auto-filled from API:**
- Name, Mobile, Aadhaar, DOB → auto-calculates Age
- Gender (M/F normalised from string)
- Title
- Permanent & Local Address, Pincode
- Card Expiry (`expiryDate`) → triggers `onCardExpiryChanged`
- `isHCRenewal` flag
- Photo URLs (only if `isReregistration = true`)

### 5.3 Renewal Logic

`onCardExpiryChanged(date)`:
- If expiry < today → `showRenewal = true`, `isHCRenewal = true`
- Else → `showRenewal = false`, clear `tecRenewalDate`

`onCardRegDateChanged(date)`:
- Resets expiry + renewal fields (user changing reg date invalidates old expiry)

### 5.4 Photo Capture

All three photos use **camera only** (`ImageSource.camera`), quality 80:
- `pickPatientPhoto()` → `patientPhotoPath`
- `pickHealthCardPhoto()` → `healthCardPhotoPath`
- `pickRenewalFormPhoto()` → `renewalFormPath` (only shown when `showRenewal`)

### 5.5 Validation (`_validateForm`) — in order

1. Reg no = 12 digits
2. Name non-empty, only `[A-Za-z ]`
3. Title selected
4. Gender selected
5. Mobile = 10 digits
6. Aadhaar = 12 digits
7. DOB non-empty
8. Age 18–60
9. Local address non-empty
10. Pincode = 6 digits
11. Card reg date + card expiry non-empty
12. Patient photo required (path or URL)
13. Health card photo required (path or URL)
14. Renewal slip required when `showRenewal = true` (path or URL)

### 5.6 Submit (`submitRegistration`)

**Endpoint:** `POST kWebservicesBaseURL/handler/BeneficiaryRe_RegistrationNew.ashx`
**Type:** `multipart/form-data`

| Field | Value |
|---|---|
| `SiteId` | `navSiteId` |
| `CampId` | `navCampId` |
| `RegdNo` | `tecWorkerRegNo.text` |
| `Title` | `selectedTitle` |
| `EnglishName` | `tecFullName.text` |
| `MobileNo` | `tecMobileNo.text` |
| `UID` | `tecAadhaarNo.text` |
| `DOB` | `tecDob.text` |
| `Age` | `tecAge.text` |
| `Gender` | `selectedGender` ('M'/'F') |
| `PermanentAddress` | `tecPermAddr.text` |
| `LocalAddress` | `tecLocalAddr.text` |
| `PinCode` | `tecPincode.text` |
| `CreatedBy` | `empCode` |
| `IsHCRenewal` | '1'/'0' |
| `RenewalDate` | `tecRenewalDate.text` (when renewal) |
| `RegistrationDate` | `tecCardRegDate.text` |
| `ExpirtyDate` | `tecCardExpiry.text` (**typo preserved** — matches server) |
| `file1` | patient photo (`{RegdNo}_PR.jpg`) |
| `file2` | health card photo (`{RegdNo}_HC.jpg`) |
| `file3` | renewal slip photo (`{RegdNo}_RS.jpg`) |

**On success:**
1. Toast message from server
2. Fire-and-forget `_syncBocw()` — syncs to BOCW external API
3. Navigate to `PatientFingerAndSignatureScreen`
4. On finger/sign success → `_clearForm()` called via `onSuccess` callback

**BOCW Sync payload** (sent to `POST mahabocw-base/bocw-registration`, JSON body):
- `RegdNo`, `RegistrationDate` (yyyy-MM-dd), `EnglishName`, `MobileNo`, `Aadhar`, `Dob`, `Age`, `Gender` (Male/Female), `DueRenewalDate`, `DISTNAME`, `Taluka`, `ResidentialAddress`, `PermanentAddress`

---

## 6. D2D Registration — Top to Bottom

### 6.1 Controller: `D2DPatientRegistrationController` (~3087 lines)

**Initialisation** (`onInit`):
- Reads `empCode`, `subOrgId` from user data
- Sets `talLgd` from user's `TALLGDCODE`
- Sets `maritalStatusId` from user's `maritialstatusId` (default '1')
- Hard-codes `tecMobileNo.text = '9322183452'` ← **TEST OVERRIDE — see §12**
- Calls `_startAutoLocationUpdates()` — GPS every 5 s
- Calls `_loadAppVersion()` — gets app version via `PackageInfo`
- Calls `_fetchFaceDetectionFlag()` — checks if face detection is mandatory

### 6.2 Four Form Scenarios

The form has four modes based on two toggles:

| Scenario | `isDependent` | `hasApiData` | What shows |
|---|---|---|---|
| 1 | No | false | Worker reg no only; no other fields |
| 2 | No | true | All fields, name/Aadhaar/gender pre-filled from API, most editable |
| 3 | Yes | false | Worker reg no + dependent controls; no name/contact |
| 4 | Yes | true | Worker info cards (read-only) at top + dependent entry fields |

`hasApiData` becomes `true` after `_fetchBeneficiary()` succeeds.

### 6.3 Worker Info Lookup (`onWorkerRegNoChanged` → `_fetchBeneficiary`)

Triggered when reg no reaches **12 digits**.

**API calls:**
1. `getWorkerInfoWithMaritalStatus(regNo)` → `WorkerInfoResponse`
2. `getWorkerRegdId(regdNo)` → `{regdId, count}`

`_workerRegdId` — internal DB ID, appended to submit `DependREGID`
`_beneficiaryCount` — appended to `RegdNo` in submit payload (`RegdNo = regNo + count`)

**If no data returned:**
- Show Marathi alert: *"बांधकाम कामगार मंडळाकडून लाभार्थ्याची अद्ययावत माहिती प्राप्त झालेली नाही..."*
- Return without filling

**365-day re-registration block** (only when `isDependent = false`):
- Calls `getReRegistrationDate(workerRegNo)` 
- If date found AND diff < 365 days → Show alert "You have done registration on {date}", clear form
- Note: sets `hasApiData = true` so ABHA section remains usable

### 6.4 Fields Applied from WorkerInfo (`_applyWorkerInfo`)

**When `isDependent = false` (Scenario 2):**
- `tecFirstName`, `tecMiddleName`, `tecLastName`, `tecFullName` ← pre-filled, UI disables
- `benefBoardName = fullName` — stored for ABHA mismatch check
- `workerGenderByPhlebo` ← normalised 'Male'/'Female' for API
- `tecMobileNo` ← from API, then **overridden to '9322183452'** (TEST)
- `originalAadhaar` ← from API, displayed masked (first 8 chars → '•')
- `tecAge`, `tecDob` ← calculated from age (API has no exact DOB)
- `selectedGender` ← 'M'/'F' from API
- `maritalStatusId`, `selectedWorkerMaritalStatusId`, `selectedWorkerMaritalStatusName`
- `tecPermAddr`, `tecLocalAddr`, `tecCurrentAddr` ← from address fields
- `tecTaluka`, `tecDistrict`, `tecLandmark`, `tecPostOffice`, `tecPincode`
- `isDistrictLocked`, `isTalukaLocked` ← true when API returned non-empty values
- `tecCardExpiry` / `tecRenewalDate` ← from `nextRenewalDate`
- `isRural`, `selectedGpName`, `selectedGpCode` ← from `isUrban`/`GPName`/`GPLGDCODE`

**When `isDependent = true` (Scenario 4):**
- Worker display cards: `workerNameDisplay`, `workerAgeDisplay`, `workerGenderDisplay`
- Identity auto-set to Aadhaar (`selectedIdentityId = '1'`, locked)
- `tecLastName` ← from API; `tecFirstName`/`tecMiddleName` are editable
- Relation list fetched via `fetchRelationList(maritalStatusId, workerGender)`

### 6.5 Dependent Flow (when `isDependent = true`)

**Step 1 — Worker reg no lookup** (same as §6.3)

**Step 2 — Select Dependent button:**
1. Calls `fetchDependentList()` → `GetDependentDetailsFromBoardData` with `'MH' + regNo`
2. Shows bottom sheet with dependent list
3. Before showing, calls `checkDependentRegistrationStatus` and `checkRelationWiseCount`
4. If blocked → show error message, do NOT select

**`onDependentSelected(dep)`:**
- Splits name into first/middle/last (middle cleared for rel IDs 1, 2, 21, 22)
- Auto-fills relation from API `relId`
- Auto-sets gender by relation ID:
  - Male gender: relIds `{1, 5, 7, 9, 17, 22}` → locked
  - Female gender: relIds `{2, 6, 8, 10, 18, 21}` → locked
  - Others: free selection
- Converts DOB from `dd-MM-yyyy` → `yyyy/MM/dd`
- `bocwIdDepend` ← `dep.bocwIdDepend` (sent as `Bocw_idDepend` in submit)

### 6.6 Identity Card Selection (Dependent only)

- `fetchIdentityList()` → `GetDocumentTypeList` API
- Auto-locked to Aadhaar when API data loaded (`isIdentityLockedByData = true`)
- **`isAadhaarMode`** getter: true when identity = '0' (none) or '1' (Aadhaar) or name contains 'aadh'
- **`identityMaxLength`**: Aadhaar/default=12, PAN=10, Driving Licence=16

### 6.7 Aadhaar Masking

- `originalAadhaar` — stores raw 12-digit string (never shown directly)
- Display: first 8 chars → '•', last 4 visible
- `onAadhaarChanged(value)` — custom input handler with `_isAadhaarUpdating` guard to prevent feedback loops
- `toggleAadhaarVisibility()` — shows/hides full number
- Full Verhoeff checksum validation: `_isValidAadhaar()` → pattern + `_verhoeffValidate()`
- Inline error shown after 12 digits entered: `aadhaarError`

### 6.8 GPS Location Capture

- `_startAutoLocationUpdates()` — called in `onInit`, repeats every 5 s
- Flow:
  1. Last known location (instant) → show coords immediately
  2. Fresh high-accuracy fix (15 s timeout)
  3. Reverse geocode via Google Maps API (`AIzaSyDbtPLpwrcS571PfdJw9ednQAemxBiNhUA`)
- Fields: `currentLat`, `currentLong`, `currentAddress`
- **Submit guard:** If both lat+long = '0.0' → block submit with alert

### 6.9 Gram Panchayat

- Toggle: `isRural` (true=Rural, false=Urban)
- Rural: must select GP from `fetchAndShowGpPicker()` → `getGramPanchayatList(talLgd)`
- Urban: `selectedGpCode = '0'`
- Sent as `GPLGDCODE` in submit (`'0'` when urban)

### 6.10 Mobile OTP Flow

- `sendMobileOtp()` → generates 5-digit OTP locally → `sendOtp()` API
- `verifyMobileOtp(otp)` → `verifyOtp()` API
- State: `mobileOtpSent`, `mobileOtpVerified`
- **Required for `without_abha` submit** (unless `altMobileOtpVerified`)

**Alternate Mobile OTP:**
- `tecAltMobileNo` → shows Marathi info alert on first character
- Same send/verify flow as primary
- State: `altMobileOtpSent`, `altMobileOtpVerified`
- If alternate number entered, must be 10 digits AND verified before submit

### 6.11 Face Detection

- `_fetchFaceDetectionFlag()` → `GetFaceDetectionFlag` API
  - Response `IsFaceDetetctionEnabled == "0"` → show skip toggle (`showFaceDetectionToggle = true`)
  - Response `"1"` → toggle hidden, face detection mandatory
- `skipFaceDetection` (default `false` = detection ON)
- `pickPatientPhoto()`:
  - `skipFaceDetection = false` → launch `FaceDetectionScreen` (liveness check)
  - `skipFaceDetection = true` → regular camera

### 6.12 District/Taluka Dropdowns

- `fetchRegDistrictList()` → `getDistrictListForReg()` — cached (only fetches once)
- `fetchRegTalukaList()` → `getTalukaListForReg(distLgdCode)` — re-fetched on district change
- `isDistrictLocked` / `isTalukaLocked` — true when API pre-filled; UI shows read-only
- `selectRegDistrict(d)` → sets district text, clears taluka
- `selectRegTaluka(t)` → sets `talLgd` from `TALLGDCODE`

### 6.13 Validation (`_validateForm`) — in order

1. GPS: lat & long both not '0.0'
2. Rural → GP selected
3. Reg no = 12 digits
4. `isDependent` → dependent selected
5. Name: dependent needs first+middle+last; worker needs fullName
6. Gender selected
7. Age: dependent → relation-specific rules; worker → 18–60
8. Mobile = 10 digits
9. `without_abha` → mobile OTP verified (or alt OTP verified)
10. Alternate mobile (if entered) = 10 digits + verified
11. Aadhaar: Verhoeff check when needed (dependent, or isAadhaarMode, or aadhaarSetForAbha)
12. DOB non-empty
13. Permanent + Local address non-empty
14. Dependent: current address ≥ 10 chars; landmark + taluka + district non-empty
15. Pincode = 6 digits
16. `with_abha` → ABHA number/address filled + verified + name/gender not changed since verify
17. Ration card required when: `isDependent = true` OR `without_abha`
    - Dependent: 3–15 chars, no all-same-digit string
18. Patient photo required when `skipFaceDetection = false`
19. Health/identity card photo always required

### 6.14 Submit (`submitRegistration`)

**Endpoint:** `POST kWebservicesBaseURL/handler/BeneficiaryRegistrationNew.ashx`
**Type:** `multipart/form-data`

Key fields:

| Field | Value | Notes |
|---|---|---|
| `RegdNo` | `regNo + _beneficiaryCount` | Count from `GetBenificiaryRegisterOrNot` |
| `IsDependent` | '1'/'0' | |
| `ReleationID` | `selectedRelation.relId` | Default '0' |
| `DependREGID` | `_workerRegdId` when dependent, else '0' | |
| `IndentityId` | `selectedIdentityId` | |
| `CW_WorkerName` | worker name (dependent) or full name | |
| `next_renewal_date` | card expiry in `yyyy-MM-dd` | |
| `CurrentAddress` | `tecCurrentAddr.text` | |
| `LandMark` | `tecLandmark.text` | |
| `AlternateMobNo` | `tecAltMobileNo.text` | |
| `IsMobNoVerified` | '0' | Native hardcodes "0" |
| `IsSelfMobNo` | '0' when `isNumberNotBelongsToBeneficiary`, else '1' | |
| `MobNoOf` | `altMobileBelongsTo` (1=Self, 2=Spouse, 3=Child) | |
| `OptionMode` | '2' | Hardcoded |
| `VersionNo` | '9.79' | Hardcoded — must match server minimum |
| `Isrecollection` | '1' when `navType == '5'` | |
| `MaritalStatusID` | `maritalStatusId` | |
| `IsFaceDetectionEnabled` | '0'/'1' | Inverted from `skipFaceDetection` |
| `TALLGDCODE` | `talLgd` | |
| `DISTLGDCODE` | `navDistLgd` | |
| `IsRegdByCall` | '1' when `navType == '7'` | |
| `Latitude` / `Longitude` | from GPS | |
| `GPLGDCODE` | GP code or '0' | |
| `ABHANumber` / `ABHAAddress` | from ABHA fields | |
| `IsWhatsAppNo` | `whatsAppMode` | |
| `WorkerGenderByPhlebo` | 'Male'/'Female' | |
| `WorkerAgeByPhlebo` | worker's age | |
| `IsFaceMatchFlag` | same as `IsFaceDetectionEnabled` | Sent twice, native behaviour |
| `Bocw_idDepend` | `bocwIdDepend` or '0' | |
| `RationCardNo` | text or 'NA' | 'NA' when field is empty |
| `file1` | patient photo | named `{RegdNo}_PR.jpg` |
| `file2` | health card photo | named `{RegdNo}_HC.jpg` |
| `file3` | renewal slip (when renewal) | |
| `file4` | HIV letter photo | |

**On success:**
1. Toast message
2. Navigate to `PatientFingerAndSignatureScreen` with patient details pre-filled
3. On finger/sign success → `RationCardPhotoScreen` (when `bocwIdDepend` non-empty)
4. On ration card upload → `_clearForm()`

### 6.15 Dependent Age Validation Rules

Mirrors native `D2DPatientRegistration_Activity.java` exactly:

| Relation | Rel IDs | Rule |
|---|---|---|
| Son / Daughter | 5, 6, 7, 8 | Age 10–17; age diff from worker ≥ 15 years |
| Father/Mother/In-laws | 1, 2, 21, 22 | Age ≥ 18 AND age > worker's age |
| Spouse & others | All others | Age 18–75 |

---

## 7. ABHA Integration (D2D Only)

### 7.1 Registration Type Toggle

`registrationType`: `'without_abha'` (default) or `'with_abha'`

Switching back to `'without_abha'` resets all ABHA state via `onRegistrationTypeChanged`.

### 7.2 ABHA Sub-Modes

| Observable | Values | Meaning |
|---|---|---|
| `abhaCreateMode` | `'aadhaar_otp'` / `'demographic'` | How ABHA is created |
| `abhaSearchMode` | `'find'` / `'verify'` | Find existing vs verify known ABHA |
| `abhaValidateMode` | `'mobile'` / `'aadhaar'` | OTP via mobile or Aadhaar |

### 7.3 Find ABHA Flow (most common)

1. `abhaSearchMode = 'find'`, `abhaValidateMode = 'mobile'`
2. Enter mobile → `sendAbhaOtp()` → `_searchAbhaByMobile(mobile)`
3. `_refreshAbhaSession()` if no token (creates ABDM session + public cert)
4. `findAbhaByMobile()` → ABDM API returns list of ABHA accounts
5. `_showAbhaSelectionDialog()` — user picks account
6. `_sendAbhaMobileOtpToSelectedIndex()` — OTP sent to selected account's mobile
7. User enters OTP → `verifyAbhaOtp()` → `_verifyFindAbhaMobileOtp()`
8. On success → `getAbhaAccountProfile()` → `fillFromAbhaCreation()`
9. `fillFromAbhaCreation()` checks board/ABHA name match → fills form or returns mismatch message

**Alternatively — Find by Aadhaar:**
1. `abhaValidateMode = 'aadhaar'`
2. Same flow but `sendAbhaAadhaarLoginOtp()` / `verifyAbhaAadhaarLoginOtp()`

### 7.4 Verify ABHA Flow (when user already has ABHA number/address)

1. `abhaSearchMode = 'verify'`
2. Enter ABHA number or address
3. OTP via mobile or Aadhaar
4. Same verify flow as Find mode

### 7.5 ABHA Create Flow (new ABHA for beneficiary)

- Opens `AbhaCreationScreen` (separate activity)
- On success → `AbhaSuccessScreen`
- `AbhaSuccessScreen` calls back `fillFromAbhaCreation()` on the controller

### 7.6 Name Mismatch Check (`fillFromAbhaCreation`)

**Non-dependent:** Checks if ABHA full name contains the board name (case-insensitive).
- Mismatch → show dialog with both names, clear ABHA search

**Dependent:** Checks only the last word of the name.
- Mismatch → show dialog
- Match → fill first/middle/last from ABHA

### 7.7 ABHA OTP Timer

- 120 seconds for first OTP
- 60 seconds for subsequent OTPs (resend)
- Max 3 resends (`abhaResendCount`)
- `_startAbhaOtpTimer(seconds)` — countdown via `Timer.periodic`

### 7.8 ABHA Session Management

- `_findAbhaAccessToken` — ABDM session access token
- `_findAbhaPublicKey` — RSA public certificate for OTP encryption
- Auto-refresh on `900901` / "Invalid Credentials" error (one retry per call)
- `_refreshAbhaSession()` → `createAbhaSession()` + `getAbhaPublicCertificate()`

### 7.9 Lock/Unlock After ABHA Fill

- `abhaFormLocked = true` after ABHA creation fill-back
- "Clear ABHA" button → `clearAfterAbhaFill()` (clears ABHA fields, NOT name fields — mirrors native `clearPatientDetails(flag=3)`)
- Submit validates name/gender haven't changed since ABHA verify (`_abhaNameAtVerify`, `_abhaGenderAtVerify`)

---

## 8. Shared Sub-Screens

### 8.1 PatientFingerAndSignatureScreen

**File:** `screen/patient_finger_signature_screen.dart`
**Controller:** `PatientFingerSignatureController`

**Parameters (all optional except campId/siteId/regNo):**

| Param | Source |
|---|---|
| `campId` | from reg screen |
| `siteId` | from reg screen |
| `regNo` | worker reg no |
| `onSuccess` | callback to clear parent form |
| `prefillRegdId` | from D2D save response (`result.regdId`) |
| `prefillName`, `prefillGender`, `prefillAge`, `prefillDob` | from D2D form |
| `rationCardNumber` | from `tecRationCardNo` |
| `dependentBocId` | triggers ration card screen when non-empty |

**Flow:**
1. Fetch patient details (if prefill not given, fetches from API by reg no)
2. Show patient info card (name, gender, age, DOB)
3. Capture thumb print (optional, can mark as device issue)
4. Capture signature → `PatientSignatureScreen` (drawing pad)
5. Submit both to `InsertSignatureandThumbDetails.ashx`

**Endpoint:** `POST kWebservicesBaseURL/InsertSignatureandThumbDetails.ashx`

Fields: `RegdId`, `SiteId`, `CampId`, `IsSignature` (1/0), `IsDeviceIssue` (0/1 — inverted), `CreatedBy`, `File1` (thumb), `File2` (signature)

> **Note:** `IsDeviceIssue` is **inverted** — `isFingerPrintIssue=true` → sends `'0'`

### 8.2 RationCardPhotoScreen (D2D Only)

**Trigger:** Shown after successful finger+sign when `dependentBocId` is non-empty.

**Endpoint:** `POST kWebservicesBaseURL/InsertRationCardDetails.ashx`

Fields: `RegdID`, `UserId`, `Bocw_Dependent_Id`, `RationCardNo`, `RCID='0'`, `RationCardImage` (file)

---

## 9. APIs — Full Reference

### Base URLs (defined in `APIManager.dart`)

| Constant | URL | Usage |
|---|---|---|
| `kWebservicesBaseURL` | `https://[server]/WebServices/` | Most endpoints |
| `kD2DBaseURL` | `https://[server]/D2DRegistration/` | Camp lists, district |
| `kConstructionWorkerBaseURL` | `https://[server]/ConstructionWorkerService/` | Worker info, re-reg check |
| `kMahabocwBaseURL` | `https://mahabocw.in/` | External BOCW sync |
| ABDM | `https://healthidsbx.abdm.gov.in/api/` | ABHA creation/linking |

### All Endpoints Used in Registration

| API Constant | Endpoint String | Method | Used By |
|---|---|---|---|
| `kGetApprovedCampListDetailsForAppFlexiCampV1` | `GetApprovedCampListDetailsForAppFlexiCampV1` | POST | Regular camp picker |
| `kGetCampDetailsonLabForDoorToDoorV2` | `GetCampDetailsonLabForDoorToDoorV2` | POST | D2D camp picker |
| `kBindDistrict` | `BindDistrict` | POST | D2D district list |
| `kGetUserCampMappingRegularCampClose` | `GetUserCampMappingRegularCampClose` | POST | Attendance check |
| `kGetWorkerInfroReRegistration` | `GetWorkerInfroRe_Registration` | POST | Regular 365-day check + pre-fill |
| `kGetWorkerInfroFromWorkerRegid` | `GetWorkerInfroFromWorkerRegid` | POST | Finger/sign screen patient lookup |
| `kGetBenificiaryRegisterOrNot` | `GetBenificiaryRegisterOrNot` | POST | Get `regdId` + `count` for D2D |
| *(D2D worker info)* | `GetWorkerInfoWithMaritalStatus` | POST | D2D worker lookup |
| `kGetDependentDetailsFromBoardData` | `GetDependentDetailsFromBoardData` | POST | Dependent list |
| `kCheckDependentRegistrationStatus` | `CheckDependentRegistrationStatus` | POST | Validate dependent can register |
| `kGetRelationWiseDependantCountwithMaritalStatus` | `GetRelationWiseDependantCountwithMaritalStatus` | POST | Check relation slot available |
| *(Document types)* | `GetDocumentTypeList` | POST | Identity card dropdown |
| *(GP list)* | `GetGramPanchayatList` | POST | Rural GP picker |
| `kGetFaceDetectionFlag` | `GetFaceDetectionFlag` | POST | Face detection mandatory check |
| *(OTP send)* | `SendOtp` | POST | Mobile / alternate mobile OTP |
| *(OTP verify)* | `VerifyOtp` | POST | OTP verification |
| *(District for reg)* | `GetDistrictListForReg` | POST | District dropdown in D2D form |
| *(Taluka for reg)* | `GetTalukaListForReg` | POST | Taluka dropdown in D2D form |
| `kInsertSignatureandThumbDetails` | `InsertSignatureandThumbDetails.ashx` | POST multipart | Finger+sign upload |
| `kInsertRationCardDetails` | `InsertRationCardDetails.ashx` | POST multipart | Ration card upload |
| *(Regular save)* | `handler/BeneficiaryRe_RegistrationNew.ashx` | POST multipart | Regular registration save |
| *(D2D save)* | `handler/BeneficiaryRegistrationNew.ashx` | POST multipart | D2D registration save |
| *(BOCW sync)* | `bocw-registration` | POST JSON | Sync to external BOCW |
| *(BOCW fetch)* | `bocw-registration/{regNo}` | GET | Fetch from external BOCW |

---

## 10. Models Reference

### `WorkerInfoOutput` (from `GetWorkerInfoWithMaritalStatus`)

Key fields used in registration:

| Field | Type | Notes |
|---|---|---|
| `firstNamePersonal` | `String?` | |
| `middleNamePersonal` | `String?` | |
| `lastNamePersonal` | `String?` | |
| `mobile` | `String?` | Overridden by test number |
| `aadhaar` | `String?` | Stored in `originalAadhaar`, displayed masked |
| `age` | `String?` | May have decimal e.g. "35.0" — use `.split('.').first` |
| `gender` | `String?` | "Male"/"Female" — normalise with `.toLowerCase().startsWith('m')` |
| `maritalStatusID` | `String?` | Drives relation list |
| `nextRenewalDate` | `String?` | Pre-fills card expiry + renewal date |
| `residentialTaluka/District/Pincode` | `String?` | Address fields |
| `permanentHouseNo/Area/PostOffice/Taluka` | `String?` | |
| `talLgdCode` | `String?` | Taluka LGD code for GP fetch |
| `distLgdCode` | `String?` | District LGD for taluka fetch |
| `gpLgdCode` / `gpName` | `String?` | GP auto-populate |
| `isUrban` | `String?` | `'0'` = Rural, non-empty & non-'0' = Urban |

**Computed getters on `WorkerInfoOutput`:**
- `fullName` → joins first+middle+last
- `localAddress` → residential address formatted
- `permanentAddressFormatted` → permanent address formatted

### `BeneficiaryOutput` (from BOCW + re-registration check)

| Field | Notes |
|---|---|
| `name` | Full name |
| `aadhaarNo` | Aadhaar |
| `dob` | Date of birth |
| `age` | May have decimal |
| `gender` | "Male"/"Female"/"M"/"F" |
| `address` / `pinCode` | Address |
| `expiryDate` | Card expiry |
| `isHCRenewal` | "Yes"/"No" |
| `patientPhotoUrl` / `healthCardPhotoUrl` / `renewalPhotoUrl` | Previous registration photos |
| `registrationDate` | Used for 365-day check |

### `D2DRegistrationResponse` (from D2D save)

Has `regdId` field — used to pre-fill `PatientFingerAndSignatureScreen` without extra API call.

### `DependentOutput` (from dependent list)

| Field | Notes |
|---|---|
| `relId` | Relation ID (String) |
| `relation` | Relation name |
| `dob` | Date in `dd-MM-yyyy` |
| `bocwIdDepend` | BOCW dependent ID — sent as `Bocw_idDepend` |
| `displayName` | Full name |

---

## 11. Business Rules & Validations

### Age Rules

| Flow | Min | Max |
|---|---|---|
| Regular (worker) | 18 | 60 |
| D2D (worker) | 18 | 60 |
| D2D (dependent spouse/other) | 18 | 75 |
| D2D (dependent child Son/Daughter) | 10 | 17 |
| D2D (dependent parents/in-laws) | 18 | no limit (must be > worker age) |

### Re-Registration Block

- 365-day check on **both** Regular and D2D
- For D2D, block only when `isDependent = false`
- Block still sets `hasApiData = true` so ABHA section remains accessible
- Alert shows the previous registration date

### Ration Card Required

- D2D `without_abha` worker: required
- D2D any dependent: required (3–15 chars, no all-same-digit)
- D2D `with_abha` non-dependent: NOT required (server has ABHA data)
- Regular: NOT used

### Version Number

- Regular: Not sent
- D2D: `VersionNo = '9.79'` hardcoded in submit
- Server checks minimum version; "lower version" error if below current minimum
- **When server upgrades** → update `VersionNo` in `submitRegistration` fields map

---

## 12. Known Gotchas & Pitfalls

### 🔴 Test Mobile Number Override
```dart
// In onInit and _applyWorkerInfo:
tecMobileNo.text = '9322183452'; // TEST OVERRIDE
```
**This is a test override.** When a future requirement says "remove 9322183452", you must revert these **two** occurrences in `d2d_patient_registration_controller.dart`:
- `onInit()` line ~298: `tecMobileNo.text = '9322183452';`
- `_applyWorkerInfo()` line ~1342: `tecMobileNo.text = '9322183452';` (after real mobile set)
- Also `_clearForm()` line ~1170: `tecMobileNo.text = '9322183452';`

And check `D2DPatientRegistration_Activity.java` for Java-side overrides.

### 🔴 `String? print` Field Shadow
`BeneficiaryWorkerOutput` (in `BeneficiaryWorkerResponse.dart`) has a field named `print`. This shadows Dart's global `print()` function. **Never call `print()` inside that class.** Use `debugPrint()` or a logger instead.

### 🔴 ABDM Timestamp Format
ABDM APIs require timestamps with `Z` suffix (e.g., `2024-01-15T10:30:00Z`).
Using `+00:00` instead of `Z` causes ABDM-1016 "Invalid Timestamp" error.

### 🔴 GetX `refresh()` Name Clash
Never name a `GetxController` method `refresh()`. GetX's `update()` calls `this.refresh()` internally — naming clash causes infinite API loop. Use `refreshData()`, `loadData()`, etc.

### 🟡 UTF-8 for Indian Language APIs
APIs returning Marathi/Devanagari text must be decoded with `utf8.decode(response.bodyBytes)`.
Dart's `http` package defaults to Latin-1 when no charset header is present, corrupting UTF-8.

### 🟡 `_beneficiaryCount` appended to RegdNo
D2D submit: `'RegdNo': '${regNo}$_beneficiaryCount'`
`_beneficiaryCount` comes from `GetBenificiaryRegisterOrNot`. It is NOT the registration count shown to users — it's an internal suffix the server expects. Do not remove it.

### 🟡 `IsDeviceIssue` is Inverted
In `insertSignatureAndThumb`: `isFingerPrintIssue=true` → sends `'0'` (not `'1'`). This matches native — field name says "issue" but value is inverted.

### 🟡 `ExpirtyDate` Typo
Regular registration submit field is `'ExpirtyDate'` (not `'ExpiryDate'`). This is a typo inherited from the server. Do NOT fix the spelling — the server expects `ExpirtyDate`.

### 🟡 V1 vs Regular Endpoint for Camp Beneficiary Details
`GetRegiWorkerDetailsOncampId_InCampTest_V1` returns extra fields (`AadharCardNo`, `RationCardNo1`, `RCImagePath1`).
The older `GetRegiWorkerDetailsOncampId_InCampTest` (without `_V1`) does **not** return these fields.
See: `APIConstants.kGetRegiWorkerDetailsOncampIdInCampTest`

### 🟡 `AppVersion` Must Be Current
Basic Health Info save API rejects with "lower version" error if `AppVersion` header is below the server's minimum. Always use the latest version string from `PackageInfo`.

### 🟡 Google Maps API Key (hardcoded)
`AIzaSyDbtPLpwrcS571PfdJw9ednQAemxBiNhUA` is hardcoded in the controller for reverse geocoding. If this key is rotated/restricted, address display breaks (coordinates shown instead).

### 🟡 `abhaOtpAttempts` vs `abhaResendCount`
- `abhaResendCount`: max 3 resends — blocks OTP send after 3
- `abhaOtpAttempts`: counts verify attempts — currently informational only, not blocked

---

*End of document. For session-specific discoveries not captured here, check `C:\Users\LENOVO\.claude\projects\...\memory\MEMORY.md`.*
