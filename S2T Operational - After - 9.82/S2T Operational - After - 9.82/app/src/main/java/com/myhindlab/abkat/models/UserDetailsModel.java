package com.myhindlab.abkat.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserDetailsModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("output")
    @Expose
    private List<Output> output;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Output> getOutput() {
        return output;
    }

    public void setOutput(List<Output> output) {
        this.output = output;
    }


    public class Output {

        @SerializedName("ID")
        @Expose
        private Integer id;
        @SerializedName("EmpCode")
        @Expose
        private Integer empCode;
        @SerializedName("SUBORGWISEDESGID")
        @Expose
        private Integer suborgwisedesgid;
        @SerializedName("SubOrgId")
        @Expose
        private Integer subOrgId;
        @SerializedName("SubOrgName")
        @Expose
        private String subOrgName;
        @SerializedName("OrgId")
        @Expose
        private Integer orgId;
        @SerializedName("OrgName")
        @Expose
        private String orgName;
        @SerializedName("ProjectId")
        @Expose
        private String projectId;
        @SerializedName("ProjectName")
        @Expose
        private String projectName;
        @SerializedName("USERNAME")
        @Expose
        private String username;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("FirstName")
        @Expose
        private String firstName;
        @SerializedName("MiddleName")
        @Expose
        private String middleName;
        @SerializedName("LastName")
        @Expose
        private String lastName;
        @SerializedName("SpouseName")
        @Expose
        private String spouseName;
        @SerializedName("Children")
        @Expose
        private Integer children;
        @SerializedName("MotherName")
        @Expose
        private String motherName;
        @SerializedName("FatherName")
        @Expose
        private String fatherName;
        @SerializedName("VoterId")
        @Expose
        private String voterId;
        @SerializedName("per_email")
        @Expose
        private String perEmail;
        @SerializedName("per_mobile")
        @Expose
        private String perMobile;
        @SerializedName("PAddress")
        @Expose
        private String pAddress;
        @SerializedName("CAddress")
        @Expose
        private String cAddress;
        @SerializedName("dob")
        @Expose
        private String dob;
        @SerializedName("AGE")
        @Expose
        private Integer age;
        @SerializedName("Aadhar")
        @Expose
        private String aadhar;
        @SerializedName("pancard")
        @Expose
        private String pancard;
        @SerializedName("ImagePath")
        @Expose
        private String imagePath;
        @SerializedName("joiningdate")
        @Expose
        private String joiningdate;
        @SerializedName("bloodgroup")
        @Expose
        private Object bloodgroup;
        @SerializedName("BLOODID")
        @Expose
        private Object bloodid;
        @SerializedName("EDUCDTLID")
        @Expose
        private Integer educdtlid;
        @SerializedName("edu")
        @Expose
        private String edu;
        @SerializedName("universityname")
        @Expose
        private String universityname;
        @SerializedName("qualification")
        @Expose
        private String qualification;
        @SerializedName("QUALID")
        @Expose
        private Object qualid;
        @SerializedName("EDTYPEID")
        @Expose
        private Object edtypeid;
        @SerializedName("UNIVID")
        @Expose
        private Object univid;
        @SerializedName("yearofpassing")
        @Expose
        private Integer yearofpassing;
        @SerializedName("percentage")
        @Expose
        private Integer percentage;
        @SerializedName("cast")
        @Expose
        private String cast;
        @SerializedName("religion")
        @Expose
        private String religion;
        @SerializedName("ReligionId")
        @Expose
        private Object religionId;
        @SerializedName("category")
        @Expose
        private String category;
        @SerializedName("Maritialstatus")
        @Expose
        private String maritialstatus;
        @SerializedName("anniversarydate")
        @Expose
        private String anniversarydate;
        @SerializedName("WorkLocationName")
        @Expose
        private String workLocationName;
        @SerializedName("Designation")
        @Expose
        private String designation;
        @SerializedName("DESGID")
        @Expose
        private Integer desgid;
        @SerializedName("WORKLOCID")
        @Expose
        private Integer worklocid;
        @SerializedName("STATELGDCODE")
        @Expose
        private Integer statelgdcode;
        @SerializedName("DISTLGDCODE")
        @Expose
        private Integer distlgdcode;
        @SerializedName("TALLGDCODE")
        @Expose
        private Integer tallgdcode;
        @SerializedName("GPLGDCODE")
        @Expose
        private Integer gplgdcode;
        @SerializedName("DESGLEVELID")
        @Expose
        private Integer desglevelid;
        @SerializedName("STATE")
        @Expose
        private String state;
        @SerializedName("district")
        @Expose
        private String district;
        @SerializedName("taluka")
        @Expose
        private String taluka;
        @SerializedName("accountno")
        @Expose
        private String accountno;
        @SerializedName("bankname")
        @Expose
        private String bankname;
        @SerializedName("BANKID")
        @Expose
        private Integer bankid;
        @SerializedName("branchname")
        @Expose
        private String branchname;
        @SerializedName("ifsccode")
        @Expose
        private String ifsccode;
        @SerializedName("OMTCSCID")
        @Expose
        private String omtcscid;
        @SerializedName("IPADDRESS")
        @Expose
        private String ipaddress;
        @SerializedName("BCompany")
        @Expose
        private String bCompany;
        @SerializedName("BAddress")
        @Expose
        private String bAddress;
        @SerializedName("BEmail")
        @Expose
        private String bEmail;
        @SerializedName("BMobile")
        @Expose
        private String bMobile;
        @SerializedName("CategoryId")
        @Expose
        private String categoryId;
        @SerializedName("MaritialstatusId")
        @Expose
        private String maritialstatusId;
        @SerializedName("HLLDISTRICTID")
        @Expose
        private Integer hlldistrictid;
        @SerializedName("pincode")
        @Expose
        private Integer pincode;
        @SerializedName("bankaddress")
        @Expose
        private String bankaddress;
        @SerializedName("CenterTypeID")
        @Expose
        private Object centerTypeID;
        @SerializedName("CenterID")
        @Expose
        private Integer centerID;
        @SerializedName("CenterName")
        @Expose
        private String centerName;
        @SerializedName("Address")
        @Expose
        private Object address;
        @SerializedName("FacilityID")
        @Expose
        private Integer facilityID;
        @SerializedName("FacilityName")
        @Expose
        private String facilityName;
        @SerializedName("LabName")
        @Expose
        private String labName;
        @SerializedName("LabIncharge")
        @Expose
        private String labIncharge;
        @SerializedName("LABAddress")
        @Expose
        private String lABAddress;
        @SerializedName("LABdetails")
        @Expose
        private String lABdetails;
        @SerializedName("TYPEID")
        @Expose
        private Integer typeid;
        @SerializedName("TYPENAME")
        @Expose
        private String typename;
        @SerializedName("passbook")
        @Expose
        private String passbook;
        @SerializedName("Gender")
        @Expose
        private Integer gender;
        @SerializedName("PatchCode")
        @Expose
        private Integer patchCode;
        @SerializedName("PatchName")
        @Expose
        private Object patchName;
        @SerializedName("CityCode")
        @Expose
        private Integer cityCode;
        @SerializedName("CityName")
        @Expose
        private String cityName;
        @SerializedName("Latitude")
        @Expose
        private Object latitude;
        @SerializedName("Longitude")
        @Expose
        private Object longitude;
        @SerializedName("AvgPatient")
        @Expose
        private Integer avgPatient;
        @SerializedName("AvgTest")
        @Expose
        private Integer avgTest;
        @SerializedName("INTERESTINFRANCH")
        @Expose
        private Boolean interestinfranch;
        @SerializedName("TYPEOFSETUP")
        @Expose
        private String typeofsetup;
        @SerializedName("NoOfBed")
        @Expose
        private Object noOfBed;
        @SerializedName("HOSPITAL_CONTACT")
        @Expose
        private Object hospitalContact;
        @SerializedName("ISLABAVAILABLE")
        @Expose
        private Object islabavailable;
        @SerializedName("LI_NAME")
        @Expose
        private String liName;
        @SerializedName("LI_MOBNO")
        @Expose
        private String liMobno;
        @SerializedName("HPID")
        @Expose
        private Object hpid;
        @SerializedName("LI_HostGender")
        @Expose
        private Object lIHostGender;
        @SerializedName("LI_Email")
        @Expose
        private Object lIEmail;
        @SerializedName("LPID")
        @Expose
        private Object lpid;
        @SerializedName("LAB_CONTACT")
        @Expose
        private String labContact;
        @SerializedName("ISTIEUPS")
        @Expose
        private Object istieups;
        @SerializedName("LabCode")
        @Expose
        private Object labCode;
        @SerializedName("ISTEST")
        @Expose
        private Object istest;
        @SerializedName("DOCID")
        @Expose
        private Object docid;
        @SerializedName("SPECIALITYID")
        @Expose
        private Object specialityid;
        @SerializedName("SPECIALITYNAME")
        @Expose
        private Object specialityname;
        @SerializedName("PRAC_SPECIALITY")
        @Expose
        private Object pracSpeciality;
        @SerializedName("SETUPTYPE")
        @Expose
        private Object setuptype;
        @SerializedName("HOS_CONTACT")
        @Expose
        private Object hosContact;
        @SerializedName("HOS_EMAILID")
        @Expose
        private Object hosEmailid;
        @SerializedName("HOS_MOBNO")
        @Expose
        private Object hosMobno;
        @SerializedName("ISATTACHED")
        @Expose
        private Object isattached;
        @SerializedName("ATTCH_HOSPITAL_NAME")
        @Expose
        private String attchHospitalName;
        @SerializedName("ISLABAVIALABLE")
        @Expose
        private Object islabavialable;
        @SerializedName("LI_DOCNAME")
        @Expose
        private String liDocname;
        @SerializedName("LI_CONTACT")
        @Expose
        private String liContact;
        @SerializedName("LI_GENDER")
        @Expose
        private Object liGender;
        @SerializedName("VlePId")
        @Expose
        private Object vlePId;
        @SerializedName("CSC_VLE_ID")
        @Expose
        private Object cscVleId;
        @SerializedName("Landlineno")
        @Expose
        private Object landlineno;
        @SerializedName("CustomerCode")
        @Expose
        private Object customerCode;
        @SerializedName("Potliuid")
        @Expose
        private Object potliuid;
        @SerializedName("Potliuser_id")
        @Expose
        private Object potliuserId;
        @SerializedName("Potliwallet_id")
        @Expose
        private Object potliwalletId;
        @SerializedName("ISASPIRANT")
        @Expose
        private Integer isaspirant;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getEmpCode() {
            return empCode;
        }

        public void setEmpCode(Integer empCode) {
            this.empCode = empCode;
        }

        public Integer getSuborgwisedesgid() {
            return suborgwisedesgid;
        }

        public void setSuborgwisedesgid(Integer suborgwisedesgid) {
            this.suborgwisedesgid = suborgwisedesgid;
        }

        public Integer getSubOrgId() {
            return subOrgId;
        }

        public void setSubOrgId(Integer subOrgId) {
            this.subOrgId = subOrgId;
        }

        public String getSubOrgName() {
            return subOrgName;
        }

        public void setSubOrgName(String subOrgName) {
            this.subOrgName = subOrgName;
        }

        public Integer getOrgId() {
            return orgId;
        }

        public void setOrgId(Integer orgId) {
            this.orgId = orgId;
        }

        public String getOrgName() {
            return orgName;
        }

        public void setOrgName(String orgName) {
            this.orgName = orgName;
        }

        public String getProjectId() {
            return projectId;
        }

        public void setProjectId(String projectId) {
            this.projectId = projectId;
        }

        public String getProjectName() {
            return projectName;
        }

        public void setProjectName(String projectName) {
            this.projectName = projectName;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getMiddleName() {
            return middleName;
        }

        public void setMiddleName(String middleName) {
            this.middleName = middleName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getSpouseName() {
            return spouseName;
        }

        public void setSpouseName(String spouseName) {
            this.spouseName = spouseName;
        }

        public Integer getChildren() {
            return children;
        }

        public void setChildren(Integer children) {
            this.children = children;
        }

        public String getMotherName() {
            return motherName;
        }

        public void setMotherName(String motherName) {
            this.motherName = motherName;
        }

        public String getFatherName() {
            return fatherName;
        }

        public void setFatherName(String fatherName) {
            this.fatherName = fatherName;
        }

        public String getVoterId() {
            return voterId;
        }

        public void setVoterId(String voterId) {
            this.voterId = voterId;
        }

        public String getPerEmail() {
            return perEmail;
        }

        public void setPerEmail(String perEmail) {
            this.perEmail = perEmail;
        }

        public String getPerMobile() {
            return perMobile;
        }

        public void setPerMobile(String perMobile) {
            this.perMobile = perMobile;
        }

        public String getPAddress() {
            return pAddress;
        }

        public void setPAddress(String pAddress) {
            this.pAddress = pAddress;
        }

        public String getCAddress() {
            return cAddress;
        }

        public void setCAddress(String cAddress) {
            this.cAddress = cAddress;
        }

        public String getDob() {
            return dob;
        }

        public void setDob(String dob) {
            this.dob = dob;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        public String getAadhar() {
            return aadhar;
        }

        public void setAadhar(String aadhar) {
            this.aadhar = aadhar;
        }

        public String getPancard() {
            return pancard;
        }

        public void setPancard(String pancard) {
            this.pancard = pancard;
        }

        public String getImagePath() {
            return imagePath;
        }

        public void setImagePath(String imagePath) {
            this.imagePath = imagePath;
        }

        public String getJoiningdate() {
            return joiningdate;
        }

        public void setJoiningdate(String joiningdate) {
            this.joiningdate = joiningdate;
        }

        public Object getBloodgroup() {
            return bloodgroup;
        }

        public void setBloodgroup(Object bloodgroup) {
            this.bloodgroup = bloodgroup;
        }

        public Object getBloodid() {
            return bloodid;
        }

        public void setBloodid(Object bloodid) {
            this.bloodid = bloodid;
        }

        public Integer getEducdtlid() {
            return educdtlid;
        }

        public void setEducdtlid(Integer educdtlid) {
            this.educdtlid = educdtlid;
        }

        public String getEdu() {
            return edu;
        }

        public void setEdu(String edu) {
            this.edu = edu;
        }

        public String getUniversityname() {
            return universityname;
        }

        public void setUniversityname(String universityname) {
            this.universityname = universityname;
        }

        public String getQualification() {
            return qualification;
        }

        public void setQualification(String qualification) {
            this.qualification = qualification;
        }

        public Object getQualid() {
            return qualid;
        }

        public void setQualid(Object qualid) {
            this.qualid = qualid;
        }

        public Object getEdtypeid() {
            return edtypeid;
        }

        public void setEdtypeid(Object edtypeid) {
            this.edtypeid = edtypeid;
        }

        public Object getUnivid() {
            return univid;
        }

        public void setUnivid(Object univid) {
            this.univid = univid;
        }

        public Integer getYearofpassing() {
            return yearofpassing;
        }

        public void setYearofpassing(Integer yearofpassing) {
            this.yearofpassing = yearofpassing;
        }

        public Integer getPercentage() {
            return percentage;
        }

        public void setPercentage(Integer percentage) {
            this.percentage = percentage;
        }

        public String getCast() {
            return cast;
        }

        public void setCast(String cast) {
            this.cast = cast;
        }

        public String getReligion() {
            return religion;
        }

        public void setReligion(String religion) {
            this.religion = religion;
        }

        public Object getReligionId() {
            return religionId;
        }

        public void setReligionId(Object religionId) {
            this.religionId = religionId;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getMaritialstatus() {
            return maritialstatus;
        }

        public void setMaritialstatus(String maritialstatus) {
            this.maritialstatus = maritialstatus;
        }

        public String getAnniversarydate() {
            return anniversarydate;
        }

        public void setAnniversarydate(String anniversarydate) {
            this.anniversarydate = anniversarydate;
        }

        public String getWorkLocationName() {
            return workLocationName;
        }

        public void setWorkLocationName(String workLocationName) {
            this.workLocationName = workLocationName;
        }

        public String getDesignation() {
            return designation;
        }

        public void setDesignation(String designation) {
            this.designation = designation;
        }

        public Integer getDesgid() {
            return desgid;
        }

        public void setDesgid(Integer desgid) {
            this.desgid = desgid;
        }

        public Integer getWorklocid() {
            return worklocid;
        }

        public void setWorklocid(Integer worklocid) {
            this.worklocid = worklocid;
        }

        public Integer getStatelgdcode() {
            return statelgdcode;
        }

        public void setStatelgdcode(Integer statelgdcode) {
            this.statelgdcode = statelgdcode;
        }

        public Integer getDistlgdcode() {
            return distlgdcode;
        }

        public void setDistlgdcode(Integer distlgdcode) {
            this.distlgdcode = distlgdcode;
        }

        public Integer getTallgdcode() {
            return tallgdcode;
        }

        public void setTallgdcode(Integer tallgdcode) {
            this.tallgdcode = tallgdcode;
        }

        public Integer getGplgdcode() {
            return gplgdcode;
        }

        public void setGplgdcode(Integer gplgdcode) {
            this.gplgdcode = gplgdcode;
        }

        public Integer getDesglevelid() {
            return desglevelid;
        }

        public void setDesglevelid(Integer desglevelid) {
            this.desglevelid = desglevelid;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public String getTaluka() {
            return taluka;
        }

        public void setTaluka(String taluka) {
            this.taluka = taluka;
        }

        public String getAccountno() {
            return accountno;
        }

        public void setAccountno(String accountno) {
            this.accountno = accountno;
        }

        public String getBankname() {
            return bankname;
        }

        public void setBankname(String bankname) {
            this.bankname = bankname;
        }

        public Integer getBankid() {
            return bankid;
        }

        public void setBankid(Integer bankid) {
            this.bankid = bankid;
        }

        public String getBranchname() {
            return branchname;
        }

        public void setBranchname(String branchname) {
            this.branchname = branchname;
        }

        public String getIfsccode() {
            return ifsccode;
        }

        public void setIfsccode(String ifsccode) {
            this.ifsccode = ifsccode;
        }

        public String getOmtcscid() {
            return omtcscid;
        }

        public void setOmtcscid(String omtcscid) {
            this.omtcscid = omtcscid;
        }

        public String getIpaddress() {
            return ipaddress;
        }

        public void setIpaddress(String ipaddress) {
            this.ipaddress = ipaddress;
        }

        public String getBCompany() {
            return bCompany;
        }

        public void setBCompany(String bCompany) {
            this.bCompany = bCompany;
        }

        public String getBAddress() {
            return bAddress;
        }

        public void setBAddress(String bAddress) {
            this.bAddress = bAddress;
        }

        public String getBEmail() {
            return bEmail;
        }

        public void setBEmail(String bEmail) {
            this.bEmail = bEmail;
        }

        public String getBMobile() {
            return bMobile;
        }

        public void setBMobile(String bMobile) {
            this.bMobile = bMobile;
        }

        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }

        public String getMaritialstatusId() {
            return maritialstatusId;
        }

        public void setMaritialstatusId(String maritialstatusId) {
            this.maritialstatusId = maritialstatusId;
        }

        public Integer getHlldistrictid() {
            return hlldistrictid;
        }

        public void setHlldistrictid(Integer hlldistrictid) {
            this.hlldistrictid = hlldistrictid;
        }

        public Integer getPincode() {
            return pincode;
        }

        public void setPincode(Integer pincode) {
            this.pincode = pincode;
        }

        public String getBankaddress() {
            return bankaddress;
        }

        public void setBankaddress(String bankaddress) {
            this.bankaddress = bankaddress;
        }

        public Object getCenterTypeID() {
            return centerTypeID;
        }

        public void setCenterTypeID(Object centerTypeID) {
            this.centerTypeID = centerTypeID;
        }

        public Integer getCenterID() {
            return centerID;
        }

        public void setCenterID(Integer centerID) {
            this.centerID = centerID;
        }

        public String getCenterName() {
            return centerName;
        }

        public void setCenterName(String centerName) {
            this.centerName = centerName;
        }

        public Object getAddress() {
            return address;
        }

        public void setAddress(Object address) {
            this.address = address;
        }

        public Integer getFacilityID() {
            return facilityID;
        }

        public void setFacilityID(Integer facilityID) {
            this.facilityID = facilityID;
        }

        public String getFacilityName() {
            return facilityName;
        }

        public void setFacilityName(String facilityName) {
            this.facilityName = facilityName;
        }

        public String getLabName() {
            return labName;
        }

        public void setLabName(String labName) {
            this.labName = labName;
        }

        public String getLabIncharge() {
            return labIncharge;
        }

        public void setLabIncharge(String labIncharge) {
            this.labIncharge = labIncharge;
        }

        public String getLABAddress() {
            return lABAddress;
        }

        public void setLABAddress(String lABAddress) {
            this.lABAddress = lABAddress;
        }

        public String getLABdetails() {
            return lABdetails;
        }

        public void setLABdetails(String lABdetails) {
            this.lABdetails = lABdetails;
        }

        public Integer getTypeid() {
            return typeid;
        }

        public void setTypeid(Integer typeid) {
            this.typeid = typeid;
        }

        public String getTypename() {
            return typename;
        }

        public void setTypename(String typename) {
            this.typename = typename;
        }

        public String getPassbook() {
            return passbook;
        }

        public void setPassbook(String passbook) {
            this.passbook = passbook;
        }

        public Integer getGender() {
            return gender;
        }

        public void setGender(Integer gender) {
            this.gender = gender;
        }

        public Integer getPatchCode() {
            return patchCode;
        }

        public void setPatchCode(Integer patchCode) {
            this.patchCode = patchCode;
        }

        public Object getPatchName() {
            return patchName;
        }

        public void setPatchName(Object patchName) {
            this.patchName = patchName;
        }

        public Integer getCityCode() {
            return cityCode;
        }

        public void setCityCode(Integer cityCode) {
            this.cityCode = cityCode;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public Object getLatitude() {
            return latitude;
        }

        public void setLatitude(Object latitude) {
            this.latitude = latitude;
        }

        public Object getLongitude() {
            return longitude;
        }

        public void setLongitude(Object longitude) {
            this.longitude = longitude;
        }

        public Integer getAvgPatient() {
            return avgPatient;
        }

        public void setAvgPatient(Integer avgPatient) {
            this.avgPatient = avgPatient;
        }

        public Integer getAvgTest() {
            return avgTest;
        }

        public void setAvgTest(Integer avgTest) {
            this.avgTest = avgTest;
        }

        public Boolean getInterestinfranch() {
            return interestinfranch;
        }

        public void setInterestinfranch(Boolean interestinfranch) {
            this.interestinfranch = interestinfranch;
        }

        public String getTypeofsetup() {
            return typeofsetup;
        }

        public void setTypeofsetup(String typeofsetup) {
            this.typeofsetup = typeofsetup;
        }

        public Object getNoOfBed() {
            return noOfBed;
        }

        public void setNoOfBed(Object noOfBed) {
            this.noOfBed = noOfBed;
        }

        public Object getHospitalContact() {
            return hospitalContact;
        }

        public void setHospitalContact(Object hospitalContact) {
            this.hospitalContact = hospitalContact;
        }

        public Object getIslabavailable() {
            return islabavailable;
        }

        public void setIslabavailable(Object islabavailable) {
            this.islabavailable = islabavailable;
        }

        public String getLiName() {
            return liName;
        }

        public void setLiName(String liName) {
            this.liName = liName;
        }

        public String getLiMobno() {
            return liMobno;
        }

        public void setLiMobno(String liMobno) {
            this.liMobno = liMobno;
        }

        public Object getHpid() {
            return hpid;
        }

        public void setHpid(Object hpid) {
            this.hpid = hpid;
        }

        public Object getLIHostGender() {
            return lIHostGender;
        }

        public void setLIHostGender(Object lIHostGender) {
            this.lIHostGender = lIHostGender;
        }

        public Object getLIEmail() {
            return lIEmail;
        }

        public void setLIEmail(Object lIEmail) {
            this.lIEmail = lIEmail;
        }

        public Object getLpid() {
            return lpid;
        }

        public void setLpid(Object lpid) {
            this.lpid = lpid;
        }

        public String getLabContact() {
            return labContact;
        }

        public void setLabContact(String labContact) {
            this.labContact = labContact;
        }

        public Object getIstieups() {
            return istieups;
        }

        public void setIstieups(Object istieups) {
            this.istieups = istieups;
        }

        public Object getLabCode() {
            return labCode;
        }

        public void setLabCode(Object labCode) {
            this.labCode = labCode;
        }

        public Object getIstest() {
            return istest;
        }

        public void setIstest(Object istest) {
            this.istest = istest;
        }

        public Object getDocid() {
            return docid;
        }

        public void setDocid(Object docid) {
            this.docid = docid;
        }

        public Object getSpecialityid() {
            return specialityid;
        }

        public void setSpecialityid(Object specialityid) {
            this.specialityid = specialityid;
        }

        public Object getSpecialityname() {
            return specialityname;
        }

        public void setSpecialityname(Object specialityname) {
            this.specialityname = specialityname;
        }

        public Object getPracSpeciality() {
            return pracSpeciality;
        }

        public void setPracSpeciality(Object pracSpeciality) {
            this.pracSpeciality = pracSpeciality;
        }

        public Object getSetuptype() {
            return setuptype;
        }

        public void setSetuptype(Object setuptype) {
            this.setuptype = setuptype;
        }

        public Object getHosContact() {
            return hosContact;
        }

        public void setHosContact(Object hosContact) {
            this.hosContact = hosContact;
        }

        public Object getHosEmailid() {
            return hosEmailid;
        }

        public void setHosEmailid(Object hosEmailid) {
            this.hosEmailid = hosEmailid;
        }

        public Object getHosMobno() {
            return hosMobno;
        }

        public void setHosMobno(Object hosMobno) {
            this.hosMobno = hosMobno;
        }

        public Object getIsattached() {
            return isattached;
        }

        public void setIsattached(Object isattached) {
            this.isattached = isattached;
        }

        public String getAttchHospitalName() {
            return attchHospitalName;
        }

        public void setAttchHospitalName(String attchHospitalName) {
            this.attchHospitalName = attchHospitalName;
        }

        public Object getIslabavialable() {
            return islabavialable;
        }

        public void setIslabavialable(Object islabavialable) {
            this.islabavialable = islabavialable;
        }

        public String getLiDocname() {
            return liDocname;
        }

        public void setLiDocname(String liDocname) {
            this.liDocname = liDocname;
        }

        public String getLiContact() {
            return liContact;
        }

        public void setLiContact(String liContact) {
            this.liContact = liContact;
        }

        public Object getLiGender() {
            return liGender;
        }

        public void setLiGender(Object liGender) {
            this.liGender = liGender;
        }

        public Object getVlePId() {
            return vlePId;
        }

        public void setVlePId(Object vlePId) {
            this.vlePId = vlePId;
        }

        public Object getCscVleId() {
            return cscVleId;
        }

        public void setCscVleId(Object cscVleId) {
            this.cscVleId = cscVleId;
        }

        public Object getLandlineno() {
            return landlineno;
        }

        public void setLandlineno(Object landlineno) {
            this.landlineno = landlineno;
        }

        public Object getCustomerCode() {
            return customerCode;
        }

        public void setCustomerCode(Object customerCode) {
            this.customerCode = customerCode;
        }

        public Object getPotliuid() {
            return potliuid;
        }

        public void setPotliuid(Object potliuid) {
            this.potliuid = potliuid;
        }

        public Object getPotliuserId() {
            return potliuserId;
        }

        public void setPotliuserId(Object potliuserId) {
            this.potliuserId = potliuserId;
        }

        public Object getPotliwalletId() {
            return potliwalletId;
        }

        public void setPotliwalletId(Object potliwalletId) {
            this.potliwalletId = potliwalletId;
        }

        public Integer getIsaspirant() {
            return isaspirant;
        }

        public void setIsaspirant(Integer isaspirant) {
            this.isaspirant = isaspirant;
        }

    }
}