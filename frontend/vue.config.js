module.exports = {
    devServer: {
        port: 9000,
        proxy: {
            "/api" : {
                target: "http://localhost:8080",
                changeOrigin: true,
                pathRewrite: {
                    '^/api': '/'
                }
            }
        }
    },
    chainWebpack: config => {
        config
            .plugin("html")
            .tap(args => {
                args[0].title = "N34 Space"
                return args
            })
    },
    publicPath: "./"
}