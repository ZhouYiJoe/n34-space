module.exports = {
    devServer: {
        port: 9000
    },
    chainWebpack: config => {
        config
            .plugin("html")
            .tap(args => {
                args[0].title = "N34 Space"
                return args
            })
    },

}