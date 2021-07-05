const {run} = require('runjs')
const chalk = require('chalk')
const config = require('../vue.config.js')
const rawArgv = process.argv.slice(2) // 获取下标从2开始的参数，如node build/index.js --preview --report则rawArgv为--preview和-report的数组
const args = rawArgv.join(' ')

// 如果当前环境配置了npm_config_preview或者包含--preview参数，则build打包工程并模拟部署到Nginx进行预览
if (process.env.npm_config_preview || rawArgv.includes('--preview')) {
    const report = rawArgv.includes('--report') // 获取--report参数

    run(`vue-cli-service build ${args}`) // build打包工程到/dist目录下

    const port = 9526
    const publicPath = config.publicPath

    var connect = require('connect')
    var serveStatic = require('serve-static')
    const app = connect()

    // 这里就是模拟Nginx的地方，它将/dist目录进行部署，就能访问打包后的文件了
    app.use(
        publicPath,
        serveStatic('./dist', {
            index: ['index.html', '/']
        })
    )

    app.listen(port, function () {
        // 打印地址进行系统访问
        console.log(chalk.green(`> Preview at  http://localhost:${port}${publicPath}`))
        // 如果有--report参数则打印地址进行webpack-bundle-analyzer的大小分析
        if (report) {
            console.log(chalk.green(`> Report at  http://localhost:${port}${publicPath}report.html`))
        }

    })
} else { // 否则直接build打包工程
    run(`vue-cli-service build ${args}`)
}
