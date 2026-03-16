class UIValidator {
  static String? validateInput(String? value) {
    if (value == null || value.isEmpty) {
      return 'Please enter username';
    }
    if (value.length < 10) {
      return 'Username must be at least 10 characters long';
    }
    return null;
  }

  static String? validatePassword(String? value) {
    if (value == null || value.isEmpty) {
      return 'Please enter password';
    }
    if (value.length < 3) {
      return 'Password must be at least 3 characters long';
    }
    return null;
  }

  static String? validateYear(String? value) {
    if (value == null || value.isEmpty) {
      return 'Please enter year';
    }
    return null;
  }

  static String? validateMonth(String? value) {
    if (value == null || value.isEmpty) {
      return 'Please enter month';
    }
    return null;
  }

  static String? validateDescription(String? value) {
    if (value == null || value.isEmpty) {
      return 'Please enter description';
    }
    return null;
  }

  static String? validateMobileNo(String? value) {
    if (value == null || value.isEmpty) {
      return 'Please enter Mobile Number';
    }
    if (value.length < 10) {
      return 'Mobile Number must be at least 10 characters long';
    }

    String pattern = r'^[6-9]{1}\d{9}$';
    RegExp regExp = RegExp(pattern);
    bool isValid = regExp.hasMatch(value);
    if (!isValid) {
      return 'Mobile Number must start with [6,7,8 or 9]';
    }
    return null;
  }

  static String? validateEmail(String? value) {
    if (value == null || value.isEmpty) {
      return 'Please enter Email';
    }

    String pattern = r'^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$';
    RegExp regExp = RegExp(pattern);
    bool isValid = regExp.hasMatch(value);
    if (!isValid) {
      return 'Email must be in this pattern [example@domain.com]';
    }
    return null;
  }

  static bool isAllZeros(String input) {
    return input.isNotEmpty &&
        input.runes.every((r) => String.fromCharCode(r) == '0');
  }
}
