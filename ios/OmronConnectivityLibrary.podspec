Pod::Spec.new do |s|
  s.name             = 'OmronConnectivityLibrary'
  s.version          = '1.0.0'
  s.summary          = 'Omron Connectivity Library for iOS'
  s.description      = 'Omron BLE Connectivity Library for iOS devices'
  s.homepage         = 'https://www.omronhealthcare.com'
  s.license          = { :type => 'Commercial' }
  s.author           = { 'Omron Healthcare' => 'omron@omronhealthcare.com' }
  s.platform         = :ios, '15.5'
  s.source           = { :path => '.' }
  s.vendored_frameworks = 'Frameworks/OmronConnectivityLibrary.xcframework'
  s.preserve_paths   = 'Frameworks/OmronConnectivityLibraryAssets.bundle'
  s.resource_bundles = { 'OmronConnectivityLibraryAssets' => ['Frameworks/OmronConnectivityLibraryAssets.bundle/**/*'] }
end