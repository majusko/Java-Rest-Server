package sk.rxjrest.endpoint;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import sk.rxjrest.dto.PagingListDto;
import sk.rxjrest.dto.user.UserDto;
import sk.rxjrest.dto.visit.VisitFormDto;
import sk.rxjrest.exception.RestException;
import sk.rxjrest.service.AuthorizationService;
import sk.rxjrest.service.VisitService;

@Controller
@RequestMapping("/visit")
public class VisitEndpoint extends BaseEndpoint {

	@Autowired
	private VisitService visitService;

	@Autowired
	private AuthorizationService authorizationService;

	@RequestMapping(method = RequestMethod.GET, value = "/search/my")
	public ResponseEntity<?> search(
			@RequestParam(value = "searchString", required = false) String searchString,
			@RequestParam(value = "plain", required = true) boolean plain,
			@RequestParam(value = "page", required = true) int page,
			@RequestParam(value = "resultsPerPage", required = true) int resultsPerPage,
			@RequestParam(value = "sort", required = true) String sort,
			@RequestParam(value = "ascSort", required = true) Boolean ascSort,
			HttpServletRequest request, HttpServletResponse response)
			throws NoSuchAlgorithmException, InvalidKeySpecException,
			SQLException, RestException {

//		try {

			final String accessToken = getAccessToken(request);
			final UserDto userDto = authorizationService.basicAuth(accessToken);
			final PagingListDto visitsListDto = visitService.searchVisitsForUser(searchString, userDto.getId(), plain, page,
					resultsPerPage, sort, ascSort);

			return new ResponseEntity<>(visitsListDto,HttpStatus.OK);

//		} catch (Exception e) {
//			return setErrorResponse(e);
//		}

	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/add")
	public ResponseEntity<?> add(
			@RequestBody VisitFormDto visitFormDto,
			HttpServletRequest request, HttpServletResponse response)
			throws NoSuchAlgorithmException, InvalidKeySpecException,
			SQLException, RestException {

//		try {

			final String accessToken = getAccessToken(request);
			final UserDto userDto = authorizationService.basicAuth(accessToken);
			final Long resultId = visitService.addVisit(visitFormDto, userDto.getId());

			return new ResponseEntity<>(resultId,HttpStatus.OK);

//		} catch (Exception e) {
//			return setErrorResponse(e);
//		}

	}
	
}
