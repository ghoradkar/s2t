// ignore_for_file: file_names

import 'dart:convert';
import 'package:flutter/foundation.dart';
import 'package:http/http.dart' as http;
import 'package:http/io_client.dart';
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'dart:async';


class Repository {
  Repository._();

  APIManager apiManager = APIManager();
  static final Repository instance = Repository._();
  static IOClient ioClient = APIManager.getInstanceOfIo1Client();

  // Default timeout duration - you can change this value
  static const Duration defaultTimeout = Duration(seconds: 120);

  static Future<http.Response> getResponse(
      String url, {
        Duration? timeout,
      }) async {
    try {
      final uri = Uri.parse(url);
      debugPrint(uri.path);

      final response = await ioClient
          .get(uri)
          .timeout(timeout ?? defaultTimeout);

      return response;
    } on TimeoutException catch (e) {
      debugPrint('GET Request timeout: $url');
      rethrow;
    } catch (e) {
      debugPrint('GET Request error: $url - $e');
      rethrow;
    }
  }

  static Future<http.Response> postResponse(
      String url,
      Map<String, dynamic> rawBody,
      Map<String, String> headers, {
        Duration? timeout,
      }) async {
    try {
      final uri = Uri.parse(url);

      final response = await ioClient
          .post(
        uri,
        body: rawBody,
        headers: headers,
        encoding: Encoding.getByName('utf-8'),
      )
          .timeout(timeout ?? defaultTimeout);

      debugPrint('POST $url');
      debugPrint('Body: ${response.body}');

      return response;
    } on TimeoutException catch (e) {
      debugPrint('POST Request timeout: $url');
      rethrow;
    } catch (e) {
      debugPrint('POST Request error: $url - $e');
      rethrow;
    }
  }

  static Future<http.StreamedResponse> postFormEncodedRequest(
      String url,
      Map<String, String> bodyFields,
      Map<String, String> headers, {
        Duration? timeout,
      }) async {
    try {
      final uri = Uri.parse(url);
      final request = http.Request('POST', uri);
      request.bodyFields = bodyFields;
      request.headers.addAll(headers);

      debugPrint('POST $url');
      debugPrint('BodyFields: $bodyFields');

      final streamedResponse = await ioClient
          .send(request)
          .timeout(timeout ?? defaultTimeout);

      return streamedResponse;
    } on TimeoutException catch (e) {
      debugPrint('POST Form Request timeout: $url');
      rethrow;
    } catch (e) {
      debugPrint('POST Form Request error: $url - $e');
      rethrow;
    }
  }

  static Future<http.StreamedResponse> getMultipartRequest(
      String url,
      Map<String, String> queryParams, {
        Duration? timeout,
      }) async {
    try {
      final uri = Uri.parse(url);
      final request = http.MultipartRequest('GET', uri);
      request.fields.addAll(queryParams);

      debugPrint('GET Multipart $url');
      debugPrint('QueryParams: $queryParams');

      return await request
          .send()
          .timeout(timeout ?? defaultTimeout);
    } on TimeoutException catch (e) {
      debugPrint('GET Multipart Request timeout: $url');
      rethrow;
    } catch (e) {
      debugPrint('GET Multipart Request error: $url - $e');
      rethrow;
    }
  }

  static Future<http.StreamedResponse> postResponseWithoutBody(
      String url, {
        Duration? timeout,
      }) async {
    try {
      var request = http.Request('POST', Uri.parse(url));

      http.StreamedResponse response = await request
          .send()
          .timeout(timeout ?? defaultTimeout);

      return response;
    } on TimeoutException catch (e) {
      debugPrint('POST Request (no body) timeout: $url');
      rethrow;
    } catch (e) {
      debugPrint('POST Request (no body) error: $url - $e');
      rethrow;
    }
  }
}

// class repository {
//   repository._();
//
//   APIManager apiManager = APIManager();
//   static final repository instance = repository._();
//   static IOClient ioClient = APIManager.getInstanceOfIo1Client();
//
//   static Future<http.Response> getResponse(String url) async {
//     final uri = Uri.parse(url);
//
//     debugPrint(uri.path);
//
//     final response = await ioClient.get(uri);
//     return response;
//   }
//
//   static Future<http.Response> postResponse(
//     String url,
//     Map<String, dynamic> rawBody,
//     Map<String, String> headers,
//   ) async {
//     final uri = Uri.parse(url);
//
//     final response = await ioClient.post(
//       uri,
//       body: rawBody,
//       headers: headers,
//       encoding: Encoding.getByName('utf-8'),
//     );
//
//     debugPrint('POST $url');
//     debugPrint('Body: ${response.body}');
//
//     return response;
//   }
//
//   static Future<http.StreamedResponse> postFormEncodedRequest(
//     String url,
//     Map<String, String> bodyFields,
//     Map<String, String> headers,
//   ) async {
//     final uri = Uri.parse(url);
//     final request = http.Request('POST', uri);
//     request.bodyFields = bodyFields;
//     request.headers.addAll(headers);
//
//     debugPrint('POST $url');
//     debugPrint('BodyFields: $bodyFields');
//
//     final streamedResponse = await ioClient.send(request);
//     return streamedResponse;
//   }
//
//   static Future<http.StreamedResponse> getMultipartRequest(
//     String url,
//     Map<String, String> queryParams,
//   ) async {
//     final uri = Uri.parse(url);
//     final request = http.MultipartRequest('GET', uri);
//     request.fields.addAll(queryParams);
//
//     debugPrint('GET Multipart $url');
//     debugPrint('QueryParams: $queryParams');
//
//     return await request.send();
//   }
//
//   static Future<http.StreamedResponse> postResponseWithoutBody(
//     String url,
//   ) async {
//     var request = http.Request('POST', Uri.parse(url));
//
//     http.StreamedResponse response = await request.send();
//     return response;
//   }
// }
