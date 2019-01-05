window.addEventListener('load', function() {
  if (typeof window.resultsForm !== 'undefined') {
    if (!navigator.geolocation){
      setView('error');
    }

    navigator.getCurrentPosition = navigator.geolocation.getCurrentPosition.bind(navigator.geolocation);

    function setView(visible) {
      window.beforeLocationDetermined.classList.toggle('is-hidden', visible !== 'before')
      window.afterLocationDetermined.classList.toggle('is-hidden', visible !== 'after')
      window.locationError.classList.toggle('is-hidden', visible !== 'error')
    }

    function success(position) {
      if (position.coords.latitude != window.latitude.value || position.coords.longitude != window.longitude.value) {
        window.latitude.value = position.coords.latitude;
        window.longitude.value = position.coords.longitude;
        setView('after');
      }
    }

    function error() {
      setView('error');
    }

    navigator.geolocation.watchPosition(success, error, {
      maximumAge: 1,
      enableHighAccuracy: true
    });
  }
});
