Pod::Spec.new do |s|
  s.name             = 'SafeySpirometerSDK'
  s.version          = '1.0.0'
  s.summary          = 'Safey Spirometer SDK for iOS'
  s.description      = 'Safey BLE Spirometer SDK for iOS devices'
  s.homepage         = 'https://safey.co'
  s.license          = { :type => 'Commercial' }
  s.author           = { 'Safey' => 'info@safey.co' }
  s.platform         = :ios, '15.5'
  s.source           = { :path => '.' }
  s.vendored_libraries = 'Frameworks/libSafeySpirometerSDK.a'
  s.preserve_paths   = 'Frameworks/SafeySpirometerSDK.swiftmodule'
  s.user_target_xcconfig = { 'SWIFT_INCLUDE_PATHS' => '$(inherited) $(PODS_ROOT)/../Frameworks' }
  s.frameworks       = 'CoreBluetooth'
end
