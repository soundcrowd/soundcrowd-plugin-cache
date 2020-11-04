# soundcrowd-plugin-cache

Cache plugin for soundcrowd

[![Build Status](https://travis-ci.org/soundcrowd/soundcrowd-plugin-cache.svg?branch=master)](https://travis-ci.org/soundcrowd/soundcrowd-plugin-cache)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/47e97f25c5c6416087626c3317e3a128)](https://www.codacy.com/app/tiefensuche/soundcrowd-plugin-cache?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=soundcrowd/soundcrowd-plugin-cache&amp;utm_campaign=Badge_Grade)
[![Maintainability](https://api.codeclimate.com/v1/badges/c5956850711d4a96f681/maintainability)](https://codeclimate.com/github/soundcrowd/soundcrowd-plugin-cache/maintainability)
[![GitHub release](https://img.shields.io/github/release/soundcrowd/soundcrowd-plugin-cache.svg)](https://github.com/soundcrowd/soundcrowd-plugin-cache/releases)
[![GitHub](https://img.shields.io/github/license/soundcrowd/soundcrowd.svg)](LICENSE)

This soundcrowd plugin adds caching support for online media providers. Once a media file was streamed completely, it is cached locally and can be played directly again without using network traffic. The cache plugin adds its own category where all cached files can be accessed. The caching process greatly depends on the `AndroidVideoCache` library, thanks!

## Building

    $ git clone --recursive https://github.com/soundcrowd/soundcrowd-plugin-cache
    $ cd soundcrowd-plugin-cache
    $ ./gradlew assembleDebug

Install via ADB:

    $ adb install cache/build/outputs/apk/debug/cache-debug.apk

## License

Licensed under GPLv3.

## Dependencies

- [AndroidVideoCache](https://github.com/danikula/AndroidVideoCache) - Apache 2.0