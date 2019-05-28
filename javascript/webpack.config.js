var fs = require('fs');
var entry = {};

// 判断制定路径是否是文件
function isFile(file) {
    return fs.statSync(file).isFile();
}

var start = (__dirname + '/src/').length;

function parseEntries(entry, dir) {
    var files = fs.readdirSync(dir);
    for (var i = 0; i < files.length; i++) {
        var file = files[i];
        file = dir + '/' + file;
        if (isFile(file)) {
            if (file.endsWith('Page.jsx') && !file.endsWith('/Page.jsx')) {
                var end = file.lastIndexOf('.jsx');
                entry[file.substr(start, end - start)] = file
            }
        } else {
            parseEntries(entry, file)
        }
    }
}

parseEntries(entry, __dirname + '/src');

console.log(JSON.stringify(entry));

module.exports = {
    entry: entry,
    module: {
        rules: [
            {
                test: /\.jsx$/,
                exclude: /(node_modules|bower_components)/,//排除掉node_module目录
                use: {
                    loader: 'babel-loader',
                    options: {
                        presets: ['@babel/preset-react'] //转码规则
                    }
                }
            },
            {
                test: /\.(gif|jpg|png|woff|svg|eot|ttf)\??.*$/,
                loader: 'url-loader?limit=1048576&name=images/[hash:8].[name].[ext]'
            },
            // {
            //     test: /\.png$/,
            //     loader: "file-loader?name=images/[hash:8].[name].[ext]"
            // }
        ]
    },
    output: {
        filename: '[name].js',
        path: __dirname + '/../orange/src/main/assets'
    }
}
