if (typeof window.resultsForm !== 'undefined') {
  if (!navigator.geolocation){
    alert("This doesn't work without your location");
  }

  function success(position) {
    window.latitude.value = position.coords.latitude;
    window.longitude.value = position.coords.longitude;
    window.resultsForm.submit();
  }

  function error() {
    alert("This doesn't work without your location");
  }

  navigator.geolocation.getCurrentPosition(success, error);
}

