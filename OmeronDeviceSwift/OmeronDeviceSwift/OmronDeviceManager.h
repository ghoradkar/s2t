//
//  OmronDeviceManager.h
//  OmeronDeviceSwift
//
//  Created by Sandeep Parmar on 19-11-2025.
//

#import <Foundation/Foundation.h>
#import <OmronConnectivityLibrary/OmronConnectivityLibrary.h>


@protocol OmronDeviceManagerDelegate <NSObject>

// Delegate method
- (void)configFileStatus:(NSString *_Nullable)data;

@end



NS_ASSUME_NONNULL_BEGIN

@interface OmronDeviceManager : NSObject

@property (nonatomic, strong) NSMutableArray *deviceList;
@property (nonatomic, strong) NSMutableArray *connectedDeviceList;
@property (nonatomic, strong) NSMutableDictionary *deviceConfig;
@property (nonatomic, strong) NSMutableDictionary *omronCommonDevice;

@property (nonatomic, weak) id<OmronDeviceManagerDelegate> omronDevicedelegate;

+ (instancetype)sharedInstance;
-(void)setOmronKey:(NSString *)apiKey;
@end

NS_ASSUME_NONNULL_END
