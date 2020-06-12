/**
 * showdown extensions by excing
 * youtube extension by the showdownjs team
 * slightly modified by Zzzyt
 * 
 * see 
 * https://github.com/excing/showdown-extensions
 * https://github.com/showdownjs/youtube-extension
 */
(function (extension) {
  'use strict';

  if (typeof showdown !== 'undefined') {
    // global (browser or nodejs global)
    extension(showdown);
  } else if (typeof define === 'function' && define.amd) {
    // AMD
    define(['showdown'], extension);
  } else if (typeof exports === 'object') {
    // Node, CommonJS-like
    module.exports = extension(require('showdown'));
  } else {
    // showdown was not found so we throw
    throw Error('Could not find showdown library');
  }

}(function (showdown) {
  'use strict';

  /** 
   * Support editing of mathematical formulas, syntax reference LaTeX.
   */
  var latexCodeBlocks = [];
  
  showdown.extension('mathjax', function () {
    return [
      {
        type:    'lang',
        regex:   '(?:^|\\n)\u00a8D\u00a8D(.*)\\n([\\s\\S]*?)\\n\u00a8D\u00a8D',
        replace: function (match, leadingSlash, codeblock) {
          // Check if we matched the leading \ and return nothing changed if so
          if (leadingSlash === '\\') {
            return match;
          } else {
            return '\n\n~Z' + (latexCodeBlocks.push({text: match.substring(1), codeblock: codeblock}) - 1) + 'Z\n\n';
          }
        }
      },

      {
        type:    'lang',
        regex:   '\u00a8D([^`\\f\\n\\r]+?)\u00a8D',
        replace: function (match, leadingSlash, codeblock) {
          // Check if we matched the leading \ and return nothing changed if so
          if (leadingSlash === '\\') {
            return match;
          } else {
            return '~Z' + (latexCodeBlocks.push({text: match, codeblock: codeblock}) - 1) + 'Z';
          }
        }
      },

      {
        type:    'output',
        regex:   '~(Z)(\\d+)\\1',
        replace: function (match, leadingSlash, index) {
          // Check if we matched the leading \ and return nothing changed if so
          if (leadingSlash === '\\') {
            return match;
          } else {
            index = Number(index);
            var code = latexCodeBlocks[index].text;
            return code.replace(/\u00a8D/g, '$$');
          }
        }
      },

      // clear cache
      {
        type: 'output',
        filter: function (text, globals_converter, options) {
          latexCodeBlocks = [];

          return text;
        }
      },

    ];
  });

  /**
   * Support for the syntax of video display, syntax: ![](https://video.mp4)
   */
  showdown.extension('video', function () {
    return [

      {
        type:    'output',
        regex:   '<p><img src="(.+(mp4|ogg|webm).*?)"(.+?)\\/>',
        replace: function (match, url, format, other) {
          // Check if we matched the leading \ and return nothing changed if so
          if (url === ('.' + format)) {
            return match;
          } else {
            // src="https://image.png" alt="image alt text" title="image title" width="100" height="auto"
            // var regex = /([a-z]+)="(.*?)"/g;

            // return `<video src="${url}" ${other} controls>I am sorry; your browser does not support HTML5 video in WebM with VP8/VP9 or MP4 with H.264.</video>`;
            return `<video ${other} controls><source src="${url}" type="video/${format}">I am sorry, Your browser does not support the <code>video</code> element.</video>`;
          }
        }
      },
    ];
  });

  /**
   * Support for the syntax of video display, syntax: ![](https://video.mp4)
   */
  showdown.extension('audio', function () {
    return [

      {
        type:    'output',
        regex:   '<p><img src="(.+(mp3|ogg|wav).*?)"(.+?)\\/>',
        replace: function (match, url, format, other) {
          // Check if we matched the leading \ and return nothing changed if so
          if (url === ('.' + format)) {
            return match;
          } else {
            // src="https://image.png" alt="image alt text" title="image title" width="100" height="auto"
            // var regex = /([a-z]+)="(.*?)"/g;

            if ('mp3' === format) format = 'mpeg';

            // return `<video src="${url}" ${other} controls>I am sorry; your browser does not support HTML5 video in WebM with VP8/VP9 or MP4 with H.264.</video>`;
            return `<audio ${other} controls><source src="${url}" type="audio/${format}">I am sorry, Your browser does not support the <code>audio</code> element.</audio>`;
          }
        }
      },
    ];
    
  });

  var needCat = false;
  var catalogues = [];

  /**
   * Support <h1> to <h6> catalog display
   */
  showdown.extension('catalog', function () {
    return [

      {
        type:    'lang',
        regex:   '\\n(\\[TOC\\])\\n',
        replace: function (match) {
          needCat = true;

          return '[[[TOC]]]]]';
        }
      },

      {
        type:    'output',
        regex:   '<h\\d id="(.+?)">(.*?)<\\/h(\\d)>',
        replace: function (match, id, title, level) {
          if (needCat) {
            var title_ahref_reg = /(.*?)<a .*>(.*?)<\/a>(.*)/g;
            var title_ahref_reg_match = title_ahref_reg.exec(title);
            if (null !== title_ahref_reg_match) {
              title = title_ahref_reg_match[1] + ' ' + title_ahref_reg_match[2] + ' ' + title_ahref_reg_match[3];
            }
            catalogues.push({'id': id, 'title': title, 'level': level});
          }

          return match;
        }
      },

      {
        type: 'output',
        filter: function (text, globals_converter, options) {
          if (catalogues.length <= 0) return text;

          var catDiv = '<div class="cat" id="toc_catalog">';
          var lastLevel = 0;
          var levelCount = 0;

          for (var i = 0; i < catalogues.length; i++) {
            var cat = catalogues[i];

            if (cat.level < lastLevel) {
              var count = lastLevel - cat.level;
              if (levelCount <= count) {
                count = levelCount - 1;
              }
              for (var l = 0; l < count; l++) {
                catDiv += ('</ul>');
              }
              levelCount -= count;
            } else if (lastLevel < cat.level) {
              catDiv += ('<ul>');
              levelCount ++;
            }
            catDiv += ('<li><a href="#' + cat.id + '">' + cat.title + '</a></li>');

            lastLevel = cat.level;
          }

          catDiv += '</ul></div>';

          needCat = false;
          catalogues = [];

          return text.replace(/\[\[\[TOC\]\]\]\]\]/g, catDiv);
        }
      },
    ];
    
  });

  /**
   * Support for anchor buttons for <h1> to <h6> titles
   */
  showdown.extension('anchor', function () {
    return [

      {
        type:    'output',
        regex:   '<h\\d id="(.+?)">(.*?)<\\/h(\\d)>',
        replace: function (match, id, title, level) {

          // github anchor style
          var octicon_html = `<a class="anchor" aria-hidden="true" href="#${id}"><svg class="octicon" viewBox="0 0 16 16" version="1.1" width="16" height="16" aria-hidden="true"><path fill-rule="evenodd" d="M4 9h1v1H4c-1.5 0-3-1.69-3-3.5S2.55 3 4 3h4c1.45 0 3 1.69 3 3.5 0 1.41-.91 2.72-2 3.25V8.59c.58-.45 1-1.27 1-2.09C10 5.22 8.98 4 8 4H4c-.98 0-2 1.22-2 2.5S3 9 4 9zm9-3h-1v1h1c1 0 2 1.22 2 2.5S13.98 12 13 12H9c-.98 0-2-1.22-2-2.5 0-.83.42-1.64 1-2.09V6.25c-1.09.53-2 1.84-2 3.25C6 11.31 7.55 13 9 13h4c1.45 0 3-1.69 3-3.5S14.5 6 13 6z"></path></svg></a>`;

          return `<h${level} id="${id}">${octicon_html}${title}</h${level}>`;
        }
      },

    ];
    
  });
  
  /**
   * Replace youtube links with video iframes
   */
  function parseYoutubeDimensions(rest, options) {
    var width, height, d, defaultWidth, defaultHeight;

    defaultWidth = options.youtubeWidth ? options.youtubeWidth : 720;
    defaultHeight = options.youtubeHeight ? options.youtubeHeight : 540;

    if (rest) {
      width = (d = /width="(.+?)"/.exec(rest)) ? d[1] : defaultWidth;
      height = (d = /height="(.+?)"/.exec(rest)) ? d[1] : defaultHeight;
    }

    // add units so they can be used in css
    if (/^\d+$/gm.exec(width)) {
      width += 'px';
    }
    if (/^\d+$/gm.exec(height)) {
      height += 'px';
    }

    return {
      width: width,
      height: height
    };
  }

  showdown.extension('youtube', function () {
    var svg =
        '<div class="youtube-preview" style="width:%2; height:%3; background-color:#333; position:relative;">' +
        '<svg version="1.1" xmlns="http://www.w3.org/2000/svg" ' +
        '     width="100" height="70" viewBox="0 0 100 70"' +
        '     style="position: absolute; top: 50%; left: 50%; transform: translate(-50%, -50%);">' +
        '    <defs>' +
        '      <linearGradient id="grad1" x1="0%" x2="0%" y1="0%" y2="100%">' +
        '        <stop offset="0%" style="stop-color:rgb(229,45,49);stop-opacity:1" />' +
        '        <stop offset="100%" style="stop-color:rgb(191,23,29);stop-opacity:1" />' +
        '      </linearGradient>' +
        '    </defs>' +
        '    <rect width="100%" height="100%" rx="26" fill="url(#grad1)"/>' +
        '    <polygon points="35,20 70,35 35,50" fill="#fff"/>' +
        '    <polygon points="35,20 70,35 64,37 35,21" fill="#e8e0e0"/>' +
        '</svg>' +
        '<div style="text-align:center; padding-top:10px; color:#fff"><a href="%1">%1</a></div>' +
        '</div>';
    var img = '<img src="data:image/gif;base64,R0lGODlhAQABAIAAAAUEBAAAACwAAAAAAQABAAACAkQBADs=" width="%2" height="%3">';
    var iframe = '<iframe src="%1" width="%2" height="%3" frameborder="0" allowfullscreen></iframe>';
    var imgRegex = /(?:<p>)?<img.*?src="(.+?)"(.*?)\/?>(?:<\/p>)?/gi;
    var fullYoutubeRegex = /(?:(?:https?:)?(?:\/\/)?)(?:(?:www)?\.)?youtube\.(?:.+?)\/(?:(?:watch\?v=)|(?:embed\/))([a-zA-Z0-9_-]{11})/i;
    var shortYoutubeRegex = /(?:(?:https?:)?(?:\/\/)?)?youtu\.be\/([a-zA-Z0-9_-]{11})/i;
    var vimeoRegex = /(?:(?:https?:)?(?:\/\/)?)(?:(?:www)?\.)?vimeo.com\/(\d+)/;
    
    return [
      {
        // It's a bit hackish but we let the core parsers replace the reference image for an image tag
        // then we replace the full img tag in the output with our iframe
        type: 'output',
        filter: function (text, converter, options) {
          var tag = iframe;
          if (options.smoothLivePreview) {
            tag = (options.simpleImgPreview) ? img : svg;
          }
          return text.replace(imgRegex, function (match, url, rest) {
            var d = parseYoutubeDimensions(rest, options);
            var m; 
            var fUrl = '';
            if ((m = shortYoutubeRegex.exec(url)) || (m = fullYoutubeRegex.exec(url))) {
              fUrl = 'https://www.youtube.com/embed/' + m[1] + '?rel=0';
            } else if ((m = vimeoRegex.exec(url))) {
              fUrl = 'https://player.vimeo.com/video/' + m[1];
            } else {
              return match;
            }
            return tag.replace(/%1/g, fUrl).replace('%2', d.width).replace('%3', d.height);
          });
        }
      }
    ];
  });
  
  
  /**
   * bilibili html5 player extension
   * by Zzzyt
   * 
   */
  function parseBiliDimensions(rest, options) {
    var width, height, d, defaultWidth, defaultHeight;

    defaultWidth = options.bilibiliWidth ? options.bilibiliWidth : 720;
    defaultHeight = options.bilibiliHeight ? options.bilibiliHeight : 540;

    if (rest) {
      width = (d = /width="(.+?)"/.exec(rest)) ? d[1] : defaultWidth;
      height = (d = /height="(.+?)"/.exec(rest)) ? d[1] : defaultHeight;
    }

    // add units so they can be used in css
    if (/^\d+$/gm.exec(width)) {
      width += 'px';
    }
    if (/^\d+$/gm.exec(height)) {
      height += 'px';
    }

    return {
      width: width,
      height: height
    };
  }
  
  showdown.extension('bilibili', function () {
    var svg =
        '<div class="bilibili-preview" style="width:%2; height:%3; background-color:#333; position:relative;">' +
        '<svg version="1.1" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1000 1000" enable-background="new 0 0 1000 1000"' +
        '     width="200" height="140"' +
        '     style="position: absolute; top: 50%; left: 50%; transform: translate(-50%, -50%);">' +
        '    <g color="#00aaff">'+
        '        <path fill="currentColor" d="M382.3,242.2c-22.3-12.1-44.6-24.1-66.9-36.4c-37-20.1-74-40.2-111-60.4c-15-8.2-21.2-20.7-12.7-36.2c8.5-15.6,22.8-16.7,37.5-8.7c86.7,47.2,173.5,94.3,260,142.1c22.1,12.2,62.1,5,76.1-15.8c44.3-64.2,88.4-128.5,132.5-192.8c1.5-2.3,3.1-4.6,4.6-6.8C715,9.8,729.4,5.1,742.4,15.4c15,11.9,12.5,26,2.8,40.2c-18,26.2-35.9,52.5-53.7,78.6c-24.8,36.4-49.5,72.7-77.2,113.1c11.3,0,18.6,0,25.7,0c95.3,0,190.8,0,286.2,0c29.1,0,33.6,4.6,33.7,33.7c0.3,199.5,0.5,398.8,0.8,598.3c0,28.5-5.1,33.4-34.2,33.6c-7.6,0-15.3,0.8-22.8-0.2c-27.5-3.1-47.7-0.5-57.3,33.6c-8.7,30.6-41.3,46.3-70.7,43.2c-34-3.6-56.5-23.7-65.2-55.4c-5.3-19.2-14.2-21.8-31.6-21.7c-119.6,0.8-239.1,0.5-358.8,0.2c-13.8,0-21.8,1.2-25.8,18c-8.7,35.9-38.2,58.7-70.9,59.3c-33.9,0.6-63.3-21.5-73.2-58.8c-4.2-15.6-11.1-19.2-25.5-18.4c-18.6,1.1-37.3,0.5-56,0.2c-20.4-0.5-29.4-8.8-29.4-28.8c0.2-202.7,0.5-405.6,0.9-608.5c0-21,8.5-28,31.9-28c96.1-0.2,192.2,0,288.2,0c6.7,0,13.5,0,20.1,0C381.1,245.8,381.7,244.1,382.3,242.2z M913.6,866.1c0-192.2,0-382.3,0-573.3c-276.1,0-550.7,0-825.1,0c0,191.9,0,382.4,0,573.3C364.3,866.1,637.8,866.1,913.6,866.1z M250.7,914.4c-20,0-37.5,0-56.8,0c3.9,16.2,11.8,27.7,26.9,28.6C237.7,944.3,245.8,931.7,250.7,914.4z M752.7,914.2c5.7,17.3,13.5,30,30.5,28.8c15.3-1.1,22.4-13,27.1-28.8C790.4,914.2,772.9,914.2,752.7,914.2z"/>' +
        '        <path fill="currentColor" d="M501.8,826.6c-110.5,0-221.2,0-331.7,0c-32.2,0-37.3-5.1-37.3-37.3c0.2-138.7,0.3-277.3,0.6-416.2c0-29.3,6-35.3,35.1-35.3c221.8,0,443.7,0,665.5,0c29.6,0,35.6,5.9,35.8,34.8c0.5,139.4,0.8,278.7,0.9,418.2c0,29.9-6.2,35.6-37,35.8C722.9,826.6,612.3,826.6,501.8,826.6z M820.8,777.6c0-131.9,0-261.1,0-390.2c-214,0-426.4,0-638.6,0c0,131.1,0,260.3,0,390.2C395.8,777.6,607.6,777.6,820.8,777.6z"/>' +
        '        <path fill="currentColor" d="M504.7,708.4c-8.5,6-16.4,12.8-25.4,17.6c-29.6,15.6-59,14.2-86.2-4.8c-30-21-43.3-51.8-40.7-87.8c0.6-7.7,14.9-20,23.1-20.3c8-0.3,18.3,10.8,24.1,19.3c4.6,6.8,2.6,17.6,5.9,25.8c6.2,15.6,17.6,30.5,34.8,26.8c12.7-2.6,24.1-15.5,33.6-26c4.6-5.3,3.9-15.6,5.1-23.7c2.2-15.3,11-24.6,26-24.3c13.6,0.3,22.4,8.7,24.3,23.5c1.1,9.4,1.4,21.2,7,27.4c9,10.1,21.7,22.1,33.3,22.6c10.4,0.3,23.1-12.4,31.3-22.3c5.4-6.5,5.1-18.1,6.8-27.5c2.8-15,11.3-24.1,26.8-23.1c15.3,1.2,23.4,11.1,22.8,26.8c-1.5,39.5-17,71.5-53.5,89.1c-34,16.4-65.9,9.4-93.8-15.9C508.3,710.7,506.6,709.8,504.7,708.4z"/>' +
        '        <path fill="currentColor" d="M777.1,560.8c0.5,23.4-18.6,36.2-36.2,26.2c-47.2-26.9-93.6-55.3-139.9-83.7c-12.2-7.6-14.9-20-7.7-32.5c7.3-12.8,20.1-16.2,32-9.4c47.7,27.5,94.9,55.9,141.8,84.8C772.5,549.6,775.1,557.8,777.1,560.8z"/>' +
        '        <path fill="currentColor" d="M374.3,470.8c4.3,4.3,15.5,10.4,16.7,17.8c1.4,8.8-3.2,24.1-10.1,27.5c-41,21-83.1,39.9-125.4,58.7c-12.8,5.7-26.9,1.4-30.2-12.5c-2.2-9.1,1.7-26.3,8.2-29.7c42.3-21.7,85.9-40.4,129.1-60C364.5,471.8,366.7,471.9,374.3,470.8z"/>' +
        '    </g>' +
        '</svg>' +
        '<div style="text-align:center; padding-top:10px; color:#fff"><a href="%1">%1</a></div>' +
        '</div>';
    var img = '<img src="data:image/gif;base64,R0lGODlhAQABAIAAAAUEBAAAACwAAAAAAQABAAACAkQBADs=" width="%2" height="%3">';
    var iframe = '<iframe src="%1" scrolling="no" border="0" frameborder="no" framespacing="0" allowfullscreen="true" width="%2" height="%3"> </iframe>';
    var imgRegex = /(?:<p>)?<img.*?src="(.+?)"(.*?)\/?>(?:<\/p>)?/gi;
    var fullBiliRegex = /(?:(?:https?:)?(?:\/\/)?)(?:(?:www)?\.)?bilibili\.com\/av([0-9]+)\/?(?:\?p=([0-9]+))?/i;
    var shortBiliRegex = /av([0-9]+)\/?(?:\?p=([0-9]+))?/i;
    
    return [
      {
        type: 'output',
        filter: function (text, converter, options) {
          var tag = iframe;
          if (options.smoothLivePreview) {
            tag = (options.simpleImgPreview) ? img : svg;
          }
          return text.replace(imgRegex, function (match, url, rest) {
            var d = parseBiliDimensions(rest, options);
            var m; 
            var fUrl = '';
            if ((m = shortBiliRegex.exec(url)) || (m = fullBiliRegex.exec(url))) {
              fUrl = 'https://player.bilibili.com/player.html?aid=' + m[1];
              if(m[2]){
              	fUrl += '&page=' + m[2];
              }
            } else {
              return match;
            }
            return tag.replace(/%1/g, fUrl).replace('%2', d.width).replace('%3', d.height);
          });
        }
      }
    ];
  });
}));