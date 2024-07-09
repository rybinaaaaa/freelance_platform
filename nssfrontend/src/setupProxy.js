const { createProxyMiddleware } = require('http-proxy-middleware');

module.exports = function(app) {
    app.use(
        '/rest',  // Это API endpoint, к которому вы хотите обратиться
        createProxyMiddleware({
            target: 'http://localhost:8080', // Адрес вашего сервера
            changeOrigin: true
        })
    );
};