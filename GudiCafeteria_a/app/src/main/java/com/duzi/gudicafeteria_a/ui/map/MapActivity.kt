package com.duzi.gudicafeteria_a.ui.map

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.View.GONE
import android.widget.Toast
import com.duzi.gudicafeteria_a.R
import com.duzi.gudicafeteria_a.data.Cafe
import com.duzi.gudicafeteria_a.ui.cafe.CafeListViewModel
import com.duzi.gudicafeteria_a.util.Utils.bitmapDescriptorFromVector
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_map.*

class MapActivity : AppCompatActivity(), OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnMarkerDragListener,
        GoogleMap.OnMapClickListener {

    private lateinit var googleMap: GoogleMap
    private lateinit var adapter: MapAdapter
    private val lists = arrayListOf<Cafe>()
    private lateinit var prevMarker: Marker
    private val markerList = ArrayList<Marker>()
    private var flag = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        initLayout()
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        return false
    }

    override fun onInfoWindowClick(marker: Marker?) {
        // nothing
    }

    override fun onMarkerDragEnd(marker: Marker?) {
        // nothing
    }

    override fun onMarkerDragStart(marker: Marker?) {
        // nothing
    }

    override fun onMarkerDrag(marker: Marker?) {
        // nothing
    }

    override fun onMapClick(latLng: LatLng) {
        viewPagerInfo.visibility = GONE
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        googleMap.setOnMarkerClickListener(this)
        googleMap.setOnMapClickListener(this)

        lists.addAll(getViewModel().getCacheCafes())

        for(cafe in lists) {
            val marker = googleMap.addMarker(MarkerOptions()
                    .position(LatLng(cafe.build_X, cafe.build_Y))
                    .icon(bitmapDescriptorFromVector(this, R.drawable.ic_marker)))
            markerList.add(marker)
        }

        if(lists.size > 0) {
            prevMarker = markerList[0]
            val latLng = LatLng(lists[0].build_X, lists[0].build_Y)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.0f))
        }

        adapter.addAll(lists)
        adapter.notifyDataSetChanged()
    }

    private fun initLayout() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        viewPagerInfo.setPadding(32, 0, 32, 0)
        viewPagerInfo.clipToPadding = false  // padding과 clipToPadding false을 주면 다음 페이지가 조금 보임
        viewPagerInfo.pageMargin = 32
        viewPagerInfo.offscreenPageLimit = 4
        viewPagerInfo.addOnPageChangeListener(object: ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
                // nothing
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                // nothing
            }

            override fun onPageSelected(position: Int) {
                if(flag) {
                    val newLatLng = LatLng(lists[position].build_X, lists[position].build_Y)
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newLatLng, 16.0f))
                    val marker = markerList[position]

                    if(prevMarker != null) {
                        prevMarker.setIcon(bitmapDescriptorFromVector(this@MapActivity, R.drawable.ic_marker))
                    }

                    if(marker != prevMarker) {
                        marker.setIcon(bitmapDescriptorFromVector(this@MapActivity, R.drawable.ic_marker_color))
                        prevMarker = marker
                    }

                    prevMarker = marker
                    flag = true
                }
            }

        })

        adapter = MapAdapter(this, supportFragmentManager, arrayListOf())
        viewPagerInfo.adapter = adapter

        moreSearch.setOnClickListener {
            Toast.makeText(this@MapActivity, "지도 데이터 추가 로드", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getViewModel(): CafeListViewModel = ViewModelProviders.of(this).get(CafeListViewModel::class.java)
}
