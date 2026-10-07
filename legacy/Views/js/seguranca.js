(() => {
  const token = document.querySelector('meta[name="atena-csrf"]').content;
  const originalOpen = XMLHttpRequest.prototype.open;
  const originalSend = XMLHttpRequest.prototype.send;
  XMLHttpRequest.prototype.open = function (method, url, ...args) {
    this.atenaLocal = new URL(url, location.href).origin === location.origin;
    return originalOpen.call(this, method, url, ...args);
  };
  XMLHttpRequest.prototype.send = function (body) {
    if (this.atenaLocal) this.setRequestHeader('X-CSRF-TOKEN', token);
    return originalSend.call(this, body);
  };
})();
