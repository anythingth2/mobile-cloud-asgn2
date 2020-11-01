/*
 *
 * Copyright 2014 Jules White
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.magnum.mobilecloud.video;

import com.google.common.collect.Lists;
import org.magnum.mobilecloud.video.repository.Video;
import org.magnum.mobilecloud.video.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;


@Controller
public class VideoController {

    @Autowired
    private VideoRepository videoRepository;

    @RequestMapping(value = "/go", method = RequestMethod.GET)
    public @ResponseBody
    String goodLuck() {
        return "Good Luck!";
    }

    @RequestMapping(value = "/video", method = RequestMethod.GET)
    public @ResponseBody
    List<Video> getAllVideo() {
        return Lists.newArrayList(this.videoRepository.findAll());
    }

    @RequestMapping(value = "/video", method = RequestMethod.POST)
    public @ResponseBody
    Video addVideo(@RequestBody Video video) {
        this.videoRepository.save(video);
        return video;
    }

    @RequestMapping(value = "/video/{id}", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<Video> findOneVideo(@PathVariable("id") long id) {
        Video video = this.videoRepository.findOne(id);
        if (video == null) {
            return new ResponseEntity<Video>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<Video>(video, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/video/{id}/like", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<Void> pressLike(@PathVariable("id") long id, Principal p) {
        Video video = this.videoRepository.findOne(id);
        if (video == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        String username = p.getName();
        if (video.getLikedBy().contains(username)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            video.getLikedBy().add(username);
            video.setLikes(video.getLikes() + 1);
            System.out.println(video.getUrl() + " like by " + username);
            System.out.println(video.getLikes());
            this.videoRepository.save(video);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/video/{id}/unlike", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<Void> unlike(@PathVariable("id") long id, Principal p) {
        Video video = this.videoRepository.findOne(id);
        if (video == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        String username = p.getName();
        if (video.getLikedBy().contains(username)) {
            video.getLikedBy().remove(username);
            video.setLikes(video.getLikes() - 1);
            System.out.println(video.getUrl() + " unlike by " + username);
            System.out.println(video.getLikes());
            this.videoRepository.save(video);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/video/search/findByName", method = RequestMethod.GET)
    public @ResponseBody
    List<Video> searchByTitle(@RequestParam(value = "title", required = true) String title) {
        System.out.println("search " + title);
        return this.videoRepository.searchByTitle(title);
    }

    @RequestMapping(value = "/video/search/findByDurationLessThan", method = RequestMethod.GET)
    public @ResponseBody
    List<Video> searchByBelowDuration(@RequestParam(value = "duration", required = true) long duration) {
        return this.videoRepository.searchByBelowDuration(duration);
    }
}
