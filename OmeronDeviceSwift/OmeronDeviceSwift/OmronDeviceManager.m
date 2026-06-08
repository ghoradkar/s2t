//
//  OmronDeviceManager.m
//  OmeronDeviceSwift
//
//  Created by Sandeep Parmar on 19-11-2025.
//

#import "OmronDeviceManager.h"

@implementation OmronDeviceManager

+ (instancetype)sharedInstance {
    static OmronDeviceManager *sharedInstance = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        sharedInstance = [[OmronDeviceManager alloc] init];
    });
    return sharedInstance;
}


-(void)setOmronKey:(NSString *)apiKey{
    
    [[OmronPeripheralManager sharedManager]setAPIKey:apiKey options:nil];
    
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(configAvailabilityNotification:)
                                                 name:OMRONBLEConfigDeviceAvailabilityNotification
                                               object:nil];
//    [OmronP]
//    [[OmronPeripheralManager sharedManager] setAPIKey:apiKey options:nil];
}
- (void)configAvailabilityNotification:(NSNotification *)aNotification {
    
    // Remove Notification listeners
    [[NSNotificationCenter defaultCenter] removeObserver:self name:OMRONBLEConfigDeviceAvailabilityNotification object:nil];
    
    OMRONConfigurationStatus configFileStatus = (OMRONConfigurationStatus)[aNotification.object unsignedIntegerValue] ;
    
   
    
    if(configFileStatus == OMRONConfigurationFileSuccess) {
        
        NSLog(@"%@",  @"Config File Extract Success");
        
        [self loadDeviceList];
        // Check if the device list is not empty
        

        
    }else if(configFileStatus == OMRONConfigurationFileError) {
        
        NSLog(@"%@",  @"Config File Extract Failure");
        
        [self-> _omronDevicedelegate configFileStatus:@"Configuration File Error!"];
        
    }else if(configFileStatus == OMRONConfigurationFileUpdateError) {
        
        NSLog(@"%@",  @"Config File Update Failure");
        
        [self loadDeviceList];
        
        [self-> _omronDevicedelegate configFileStatus:@"Configuration Update Error!"];
    }
    
}
- (void)loadDeviceList {
    
    NSDictionary *configDictionary = [[NSDictionary alloc] initWithDictionary:[[OmronPeripheralManager sharedManager] retrieveManagerConfiguration]];
    
    self->_deviceList = [NSMutableArray arrayWithArray:[configDictionary objectForKey:OMRONBLEConfigDeviceKey]];
    
    
}

@end
