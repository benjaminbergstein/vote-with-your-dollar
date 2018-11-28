window.addEventListener('load', function() {
  if (typeof window.resultsForm !== 'undefined') {
    if (!navigator.geolocation){
      alert("This doesn't work without your location");
    }

    navigator.getCurrentPosition = navigator.geolocation.getCurrentPosition.bind(navigator.geolocation);

    function success(position) {
      window.latitude.value = position.coords.latitude;
      window.longitude.value = position.coords.longitude;
      window.resultsForm.submit();
    }

    function error() {
      alert("This doesn't work without your location");
    }

    navigator.geolocation.watchPosition(success, error, {
      maximumAge: 1,
      enableHighAccuracy: true
    });
  }
});
